package com.hubermjonathan.discord

import com.hubermjonathan.discord.mitch.MitchBot
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.slf4j.LoggerFactory

val appModule = module {
    single<JDABuilder> {
        JDABuilder.createDefault(System.getenv("MITCH_TOKEN"))
            .setChunkingFilter(ChunkingFilter.ALL)
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
    }

    single<JDA> { get<JDABuilder>().build().awaitReady() }
}

fun main() {
    val logger = LoggerFactory.getLogger("main")

    startKoin {
        modules(appModule)
    }

    MitchBot().run()

//    if (System.getenv("BOT_OWNER_ID") == null) {
//        logger.error("missing bot owner id")
//        exitProcess(1)
//    }
//
//    if (System.getenv("MITCH_TOKEN") != null) {
//        val mitchBot = MitchBot(System.getenv("MITCH_TOKEN"))
//
//        logger.info("running mitch bot")
//        mitchBot.run()
//    } else {
//        logger.error("missing mitch token")
//        exitProcess(1)
//    }
}
