package com.hubermjonathan.discord.mitch

import com.hubermjonathan.discord.mitch.groups.GroupsFeature
import com.hubermjonathan.discord.mitch.management.ManagementFeature
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("mitch")
    val jda = JDABuilder
        .createDefault(MitchConfig.MITCH_TOKEN)
        .setChunkingFilter(ChunkingFilter.ALL)
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .enableIntents(GatewayIntent.GUILD_MEMBERS)
        .build()
        .awaitReady()

    jda.purdudesGuild.updateCommands().complete()

    val features = listOf(
        GroupsFeature(jda),
        ManagementFeature(jda),
    )
    features.forEach { it.load() }

    logger.info("mitch is running with the following features: $features")
}
