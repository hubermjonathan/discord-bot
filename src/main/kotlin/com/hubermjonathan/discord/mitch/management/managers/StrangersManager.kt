package com.hubermjonathan.discord.mitch.management.managers

import com.hubermjonathan.discord.common.models.FeatureContext
import com.hubermjonathan.discord.common.models.Manager
import com.hubermjonathan.discord.mitch.management.strangersRole
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent

class StrangersManager(featureContext: FeatureContext) : Manager(featureContext) {
    private val logger = featureContext.logger

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val userIsBot = event.user.isBot

        if (userIsBot) return

        logger.info("${event.member.effectiveName} joined the server")
        event.guild
            .addRoleToMember(event.member, event.guild.strangersRole)
            .queue()
    }
}
