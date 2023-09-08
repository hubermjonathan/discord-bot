package com.hubermjonathan.discord.mitch

import com.hubermjonathan.discord.common.models.Command
import com.hubermjonathan.discord.mitch.management.ManagementFeature
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main() {
    val logger: Logger = LoggerFactory.getLogger("mitch")
    val jda = JDABuilder
        .createDefault(MitchConfig.MITCH_TOKEN)
        .setChunkingFilter(ChunkingFilter.ALL)
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .enableIntents(GatewayIntent.GUILD_MEMBERS)
        .build()
        .awaitReady()
    val features = listOf(
        ManagementFeature(jda),
    )
    val commands = features.flatMap { it.commands }

    features.forEach {
        it.load(jda)
        it.startTasks()
    }
    jda.purdudesGuild
        .setCommands(commands)
        .queue()
    logger.info("mitch is running")
}

private fun Guild.setCommands(commands: List<Command>): CommandListUpdateAction {
    return this
        .updateCommands()
        .addCommands(commands.map { it.commandData })
}
