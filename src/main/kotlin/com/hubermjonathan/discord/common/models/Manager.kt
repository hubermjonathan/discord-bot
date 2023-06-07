package com.hubermjonathan.discord.common.models

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class Manager(
    protected val context: Context,
) : ListenerAdapter(), KoinComponent {
    protected val jda: JDA by inject()
}
