package com.hubermjonathan.discord.mitch.events;

import com.hubermjonathan.discord.mitch.utils.EventUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

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
        for (TextChannel textChannel : guild.getTextChannels()) {
            for (Message eventMessage : textChannel.retrievePinnedMessages().complete()) {
                if (!eventMessage.getAuthor().equals(guild.getSelfMember().getUser())) {
                    continue;
                }

                try {
                    String eventTimestamp = eventMessage.getEmbeds().get(0).getFooter().getText();
                    LocalDateTime eventLocalDateTime = LocalDateTime.of(
                            EventUtils.getDate(eventTimestamp, null), EventUtils.getTime(eventTimestamp, null));

                    if (eventLocalDateTime.plusHours(1).isBefore(
                            LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))))) {
                        eventMessage.delete().queue();
                    }
                } catch (Exception e) {
                    System.out.println("invalid event timestamp");
                }
            }
        }
    }
}
