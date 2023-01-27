package com.hubermjonathan.discord.mitch.events.tasks;

import com.hubermjonathan.discord.common.models.Task;
import com.hubermjonathan.discord.mitch.events.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SendDailyEvents extends Task {
    public SendDailyEvents(Guild guild, Date startDate, long schedule) {
        super(guild, startDate, schedule);
    }

    @Override
    public void execute() {
        for (TextChannel textChannel : getGuild().getTextChannels()) {
            HashMap<LocalTime, Message> events = new HashMap<>();

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
                    LocalDate eventLocalDate = Util.getDate(eventTimestamp, null);
                    LocalTime eventLocalTime = Util.getTime(eventTimestamp, null);

                    if (eventLocalDate.isEqual(LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))))) {
                        events.put(eventLocalTime, eventMessage);
                    }
                } catch (Exception e) {
                    System.out.println("invalid event timestamp");
                }
            }

            if (!events.isEmpty()) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                StringBuilder stringBuilder = new StringBuilder(">>> ");
                List<LocalTime> eventTimes = new ArrayList<>(events.keySet());

                Collections.sort(eventTimes);

                for (LocalTime eventTime : eventTimes) {
                    String eventTitle = events.get(eventTime)
                            .getEmbeds()
                            .get(0)
                            .getTitle();
                    String eventUrl = events
                            .get(eventTime)
                            .getJumpUrl();
                    String formattedTime = eventTime
                            .format(DateTimeFormatter.ofPattern("h:mma"))
                            .toLowerCase();

                    stringBuilder.append(String.format("[%s @ %s](%s)\n", eventTitle, formattedTime, eventUrl));
                }

                embedBuilder.setTitle("events happening today");
                embedBuilder.setDescription(stringBuilder.toString());
                embedBuilder.setColor(0xcfb991);
                textChannel
                        .sendMessageEmbeds(embedBuilder.build())
                        .queue();
            }
        }
    }
}
