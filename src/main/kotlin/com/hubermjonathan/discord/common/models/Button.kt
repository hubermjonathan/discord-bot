package com.hubermjonathan.discord.common.models

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class Button(private val id: String, protected val context: Context) : ListenerAdapter(), KoinComponent {
    protected val jda: JDA by inject()
    private val logger = context.logger

    protected open fun shouldIgnoreEvent(event: ButtonInteractionEvent): Boolean {
        val userIsBot = event.user.isBot
        val buttonIdIsWrong = event.componentId != id

        return userIsBot ||
            buttonIdIsWrong
    }

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        if (shouldIgnoreEvent(event)) return

        try {
            execute(event)
            event
                .deferEdit()
                .queue()
        } catch (e: Exception) {
            logger.error(e.localizedMessage, e)
            event
                .reply(e.localizedMessage)
                .setEphemeral(true)
                .queue()
        }
    }

    abstract fun execute(event: ButtonInteractionEvent)
}
