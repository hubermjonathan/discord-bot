package com.hubermjonathan.discord.mitch.events.events;

import com.hubermjonathan.discord.mitch.events.EventsUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SendDailyEvents extends TimerTask {
    private final Guild guild;

    public SendDailyEvents(final Guild guild) {
        this.guild = guild;
    }

    @Override
    public void run() {
        for (final TextChannel textChannel : guild.getTextChannels()) {
            final HashMap<LocalTime, Message> events = new HashMap<>();

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
                    final LocalDate eventLocalDate = EventsUtil.getDate(eventTimestamp, null);
                    final LocalTime eventLocalTime = EventsUtil.getTime(eventTimestamp, null);

                    if (eventLocalDate.isEqual(LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))))) {
                        events.put(eventLocalTime, eventMessage);
                    }
                } catch (final Exception e) {
                    System.out.println("invalid event timestamp");
                }
            }

            if (!events.isEmpty()) {
                final EmbedBuilder embedBuilder = new EmbedBuilder();
                final StringBuilder stringBuilder = new StringBuilder(">>> ");
                final List<LocalTime> eventTimes = new ArrayList<>(events.keySet());

                Collections.sort(eventTimes);

                for (final LocalTime eventTime : eventTimes) {
                    final String eventTitle = events.get(eventTime)
                            .getEmbeds()
                            .get(0)
                            .getTitle();
                    final String eventUrl = events
                            .get(eventTime)
                            .getJumpUrl();
                    final String formattedTime = eventTime
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
