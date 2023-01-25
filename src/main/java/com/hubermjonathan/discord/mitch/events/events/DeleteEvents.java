package com.hubermjonathan.discord.mitch.events.events;

import com.hubermjonathan.discord.mitch.events.EventsUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimerTask;

public class DeleteEvents extends TimerTask {
    private final Guild guild;

    public DeleteEvents(final Guild guild) {
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
                            EventsUtil.getDate(eventTimestamp, null), EventsUtil.getTime(eventTimestamp, null)
                    );

                    if (eventLocalDateTime.plusHours(1).isBefore(LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))))) {
                        eventMessage
                                .delete()
                                .queue();
                    }
                } catch (final Exception e) {
                    System.out.println("invalid event timestamp");
                }
            }
        }
    }
}
