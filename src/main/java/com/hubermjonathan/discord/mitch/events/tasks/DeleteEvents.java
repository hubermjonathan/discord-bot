package com.hubermjonathan.discord.mitch.events.tasks;

import com.hubermjonathan.discord.common.models.Task;
import com.hubermjonathan.discord.mitch.events.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DeleteEvents extends Task {
    public DeleteEvents(Guild guild, long startTime, long schedule) {
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
                            Util.getDate(eventTimestamp, null), Util.getTime(eventTimestamp, null)
                    );

                    if (eventLocalDateTime.plusHours(1).isBefore(LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))))) {
                        eventMessage
                                .delete()
                                .queue();
                    }
                } catch (Exception e) {
                    System.out.println("invalid event timestamp");
                }
            }
        }
    }
}
