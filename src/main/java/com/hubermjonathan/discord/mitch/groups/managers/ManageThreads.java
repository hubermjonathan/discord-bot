package com.hubermjonathan.discord.mitch.groups.managers;

import com.hubermjonathan.discord.common.models.Manager;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;

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
    }
}
