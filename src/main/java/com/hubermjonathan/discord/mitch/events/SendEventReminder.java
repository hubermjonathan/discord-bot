package com.hubermjonathan.discord.mitch.events;

import com.hubermjonathan.discord.mitch.utils.EventUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

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
        for (TextChannel textChannel : guild.getTextChannels()) {
            for (Message eventMessage : textChannel.retrievePinnedMessages().complete()) {
                if (!eventMessage.getAuthor().equals(guild.getSelfMember().getUser())) {
                    continue;
                }

                try {
                    String eventTimestamp = eventMessage.getEmbeds().get(0).getFooter().getText();
                    LocalDateTime eventLocalDateTime = LocalDateTime.of(
                            EventUtils.getDate(eventTimestamp, null), EventUtils.getTime(eventTimestamp, null));

                    LocalDateTime now = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST")));
                    now = now.minusSeconds(now.getSecond()).minusNanos(now.getNano());

                    if (eventLocalDateTime.minusMinutes(30).isEqual(now)) {
                        String eventTitle = eventMessage.getEmbeds().get(0).getTitle();
                        ArrayList<String> membersGoing = new ArrayList<>(Arrays.asList(
                                eventMessage.getEmbeds().get(0).getFields().get(0)
                                        .getValue().replaceAll(">>> ", "").split("\n")));
                        StringBuilder stringBuilder = new StringBuilder();

                        if (membersGoing.get(0).equals("-----")) {
                            continue;
                        }

                        for (String member : membersGoing) {
                            stringBuilder.append(guild.getMembersByEffectiveName(member, true).get(0).getAsMention());
                            stringBuilder.append(" ");
                        }

                        eventMessage.reply(String.format("%s%s %s", stringBuilder, eventTitle, "is starting in 30 minutes!"))
                                .queue();
                    }
                } catch (Exception e) {
                    System.out.println("invalid event timestamp");
                }
            }
        }
    }
}
