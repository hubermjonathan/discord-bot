package com.hubermjonathan.discord.common.models

import com.hubermjonathan.discord.common.DiscordLogger
import net.dv8tion.jda.api.JDA

data class FeatureContext(
    val jda: JDA,
    val logger: DiscordLogger,
    val name: String,
    val icon: String,
)
