package com.hubermjonathan.discord.mitch.management.managers;

import com.hubermjonathan.discord.common.Logger;
import com.hubermjonathan.discord.common.models.Manager;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;

public class ManageThreads extends Manager {
    @Override
    public void onChannelCreate(ChannelCreateEvent event) {
        if (!event.isFromType(ChannelType.GUILD_PUBLIC_THREAD) && !event.isFromType(ChannelType.GUILD_PRIVATE_THREAD)) {
            return;
        }

        Logger.log(
                event.getJDA(),
                "\uD83D\uDC64 management",
                String.format(
                        "thread %s created in %s",
                        event.getChannel().getAsMention(),
                        event.getChannel().asThreadChannel().getParentChannel().getAsMention()
                )
        );
    }
}
