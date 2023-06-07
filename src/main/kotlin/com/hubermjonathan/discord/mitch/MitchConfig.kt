package com.hubermjonathan.discord.mitch

object MitchConfig {
    val MITCH_TOKEN = System.getenv("MITCH_TOKEN")
            ?: throw IllegalStateException("missing environment variable: MITCH_TOKEN")
    val DEV_MODE = System.getenv("DEV") != null
    val LOG_EVENTS_TO_BOT_OWNER = System.getenv("LOG_EVENTS_TO_BOT_OWNER") != null
}
