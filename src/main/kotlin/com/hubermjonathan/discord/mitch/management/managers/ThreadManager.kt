package com.hubermjonathan.discord.mitch.management.managers

import com.hubermjonathan.discord.common.models.Context
import com.hubermjonathan.discord.common.models.Manager
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent

class ThreadManager(context: Context) : Manager(context) {
    private val logger = context.logger

    override fun onChannelCreate(event: ChannelCreateEvent) {
        val channelIsNotPublicThread = !event.isFromType(ChannelType.GUILD_PUBLIC_THREAD)
        val channelIsNotPrivateThread = !event.isFromType(ChannelType.GUILD_PRIVATE_THREAD)

        if (channelIsNotPublicThread && channelIsNotPrivateThread) {
            return
        }

        logger.info("thread ${event.channel.asMention} created in ${event.channel.asThreadChannel().parentChannel.asMention}")
    }
}
