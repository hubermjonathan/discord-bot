package com.hubermjonathan.discord

import com.hubermjonathan.discord.mitch.MitchBot
import kotlin.system.exitProcess

fun main() {
    if (System.getenv("BOT_OWNER_ID") == null) {
        println("missing bot owner id")
        exitProcess(1)
    }

    if (System.getenv("MITCH_TOKEN") != null) {
        val mitchBot = MitchBot()

        println("running mitch bot")
        mitchBot.run(System.getenv("MITCH_TOKEN"))
    }
}