package com.hubermjonathan.discord.mitch.events.events;

import com.hubermjonathan.discord.mitch.events.EventsUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;

public class SendEventReminder extends TimerTask {
    private final Guild guild;

    public SendEventReminder(final Guild guild) {
        this.guild = guild;
    }

    @Override
    public void run() {
        for (final TextChannel textChannel : guild.getTextChannels()) {
            for (final Message eventMessage : textChannel.retrievePinnedMessages().complete()) {
                if (!eventMessage.getAuthor().equals(guild.getSelfMember().getUser())) {
                    continue;
                }

                try {
                    final String eventTimestamp = eventMessage
                            .getEmbeds()
                            .get(0)
                            .getFooter()
                            .getText();
                    final LocalDateTime eventLocalDateTime = LocalDateTime.of(
                            EventsUtil.getDate(eventTimestamp, null),
                            EventsUtil.getTime(eventTimestamp, null)
                    );
                    LocalDateTime now = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST")));

                    now = now
                            .minusSeconds(now.getSecond())
                            .minusNanos(now.getNano());

                    if (eventLocalDateTime.minusMinutes(30).isEqual(now)) {
                        final String eventTitle = eventMessage
                                .getEmbeds()
                                .get(0)
                                .getTitle();
                        final ArrayList<String> membersGoing = new ArrayList<>(
                                Arrays.asList(
                                    eventMessage
                                            .getEmbeds()
                                            .get(0)
                                            .getFields()
                                            .get(0)
                                            .getValue()
                                            .replaceAll(">>> ", "")
                                            .split("\n")
                                )
                        );
                        final StringBuilder stringBuilder = new StringBuilder();

                        if (membersGoing.get(0).equals("-----")) {
                            continue;
                        }

                        for (final String member : membersGoing) {
                            stringBuilder
                                    .append(
                                            guild
                                                    .getMembersByEffectiveName(member, true)
                                                    .get(0)
                                                    .getAsMention()
                                    )
                                    .append(" ");
                        }

                        eventMessage
                                .reply(String.format("%s%s %s", stringBuilder, eventTitle, "is starting in 30 minutes!"))
                                .queue();
                    }
                } catch (final Exception e) {
                    System.out.println("invalid event timestamp");
                }
            }
        }
    }
}
