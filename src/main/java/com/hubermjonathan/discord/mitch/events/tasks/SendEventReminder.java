package com.hubermjonathan.discord.mitch.events.tasks;

import com.hubermjonathan.discord.common.models.Task;
import com.hubermjonathan.discord.mitch.events.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;

public class SendEventReminder extends Task {
    public SendEventReminder(Guild guild, long startTime, long schedule) {
        super(guild, startTime, schedule);
    }

    @Override
    public void execute() {
        for (TextChannel textChannel : getGuild().getTextChannels()) {
            for (Message eventMessage : textChannel.retrievePinnedMessages().complete()) {
                if (!eventMessage.getAuthor().equals(getGuild().getSelfMember().getUser())) {
                    continue;
                }

                try {
                    String eventTimestamp = eventMessage
                            .getEmbeds()
                            .get(0)
                            .getFooter()
                            .getText();
                    LocalDateTime eventLocalDateTime = LocalDateTime.of(
                            Util.getDate(eventTimestamp, null),
                            Util.getTime(eventTimestamp, null)
                    );
                    LocalDateTime now = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST")));

                    now = now
                            .minusSeconds(now.getSecond())
                            .minusNanos(now.getNano());

                    if (eventLocalDateTime.minusMinutes(30).isEqual(now)) {
                        String eventTitle = eventMessage
                                .getEmbeds()
                                .get(0)
                                .getTitle();
                        ArrayList<String> membersGoing = new ArrayList<>(
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
                        StringBuilder stringBuilder = new StringBuilder();

                        if (membersGoing.get(0).equals("-----")) {
                            continue;
                        }

                        for (String member : membersGoing) {
                            stringBuilder
                                    .append(
                                            getGuild()
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
                } catch (Exception e) {
                    System.out.println("invalid event timestamp");
                }
            }
        }
    }
}
