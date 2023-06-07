package com.hubermjonathan.discord.mitch.management.managers

import com.hubermjonathan.discord.common.models.Context
import com.hubermjonathan.discord.common.models.Manager
import com.hubermjonathan.discord.mitch.strangersRole
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent

class StrangerManager(context: Context) : Manager(context) {
    private val logger = context.logger

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        if (event.user.isBot) return

        logger.info("${event.member.effectiveName} joined the server")
        event.guild
                .addRoleToMember(event.member, event.guild.strangersRole)
                .queue()
    }
}
