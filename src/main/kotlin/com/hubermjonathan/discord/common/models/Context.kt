package com.hubermjonathan.discord.common.models

import com.hubermjonathan.discord.common.DiscordLogger

data class Context(
    val logger: DiscordLogger,
    val name: String,
    val icon: String,
)
