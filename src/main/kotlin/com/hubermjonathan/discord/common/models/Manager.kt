package com.hubermjonathan.discord.common.models

import net.dv8tion.jda.api.hooks.ListenerAdapter

abstract class Manager(
    protected val featureContext: FeatureContext,
) : ListenerAdapter() {
    protected val jda = featureContext.jda

    open fun whenLoaded() {}
}
