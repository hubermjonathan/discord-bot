package com.hubermjonathan.discord.mitch.management.managers;

import com.hubermjonathan.discord.common.Logger;
import com.hubermjonathan.discord.common.models.Manager;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageThreads extends Manager {
    @Override
    public void onChannelCreate(ChannelCreateEvent event) {
        if (!event.isFromType(ChannelType.GUILD_PUBLIC_THREAD) && !event.isFromType(ChannelType.GUILD_PRIVATE_THREAD)) {
            return;
        }

        event
                .getChannel()
                .asThreadChannel()
                .getManager()
                .setName(String.format("\uD83D\uDCAC %s (tbd)", event.getChannel().getName().toLowerCase()))
                .queue();
        Logger.log(
                event.getJDA(),
                "\uD83D\uDC64 management",
                String.format(
                        "thread %s created in %s",
                        event.getChannel().getAsMention(),
                        event.getChannel().asThreadChannel().getParentChannel().getAsMention()
                )
        );

        List<MessageReaction> reactions = event
                .getChannel()
                .asThreadChannel()
                .retrieveParentMessage()
                .complete()
                .getReactions();

        if (reactions.size() > 0) {
            List<String> reactionMentions = new ArrayList();

            for (MessageReaction reaction : reactions) {
                reactionMentions.add(
                        String.join(
                                " ",
                                reaction.retrieveUsers().stream().map(user -> user.getAsMention()).collect(Collectors.toList())
                        )
                );
            }

            event
                    .getChannel()
                    .asThreadChannel()
                    .sendMessage(String.join(" ", reactionMentions))
                    .queue();
        }
    }
}
