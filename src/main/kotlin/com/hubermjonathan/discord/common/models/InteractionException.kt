package com.hubermjonathan.discord.common.models

import net.dv8tion.jda.api.entities.User

class InteractionException(
    override val message: String,
    val user: User,
    val interactionName: String,
    val featureContext: FeatureContext,
) : Throwable(message)
