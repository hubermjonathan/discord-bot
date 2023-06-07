package com.hubermjonathan.discord.mitch

import com.hubermjonathan.discord.common.models.Command
import com.hubermjonathan.discord.mitch.management.ManagementFeature
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main() {
    startKoin {
        modules(module {
            single<JDABuilder> {
                JDABuilder
                        .createDefault(MitchConfig.MITCH_TOKEN)
                        .setChunkingFilter(ChunkingFilter.ALL)
                        .setMemberCachePolicy(MemberCachePolicy.ALL)
                        .enableIntents(GatewayIntent.GUILD_MEMBERS)
            }

            single<JDA> {
                get<JDABuilder>()
                        .build()
                        .awaitReady()
            }
        })
    }

    MitchBot().run()
}

class MitchBot : KoinComponent {
    private val logger: Logger = LoggerFactory.getLogger("mitch")
    private val jdaBuilder: JDABuilder by inject()
    private val jda: JDA by inject()

    fun run() {
        logger.info("running mitch bot")

        val features = listOf(
            ManagementFeature(),
        )

        features.forEach {
            it.load(jdaBuilder)
        }

        val commands = features.flatMap { it.commands }

        jda.guild
                .setCommands(commands)
                .queue()

        features.forEach {
            it.startTasks()
        }
    }
}

private fun Guild.setCommands(commands: List<Command>): CommandListUpdateAction {
    return this
            .updateCommands()
            .addCommands(commands.map { it.commandData })
}
