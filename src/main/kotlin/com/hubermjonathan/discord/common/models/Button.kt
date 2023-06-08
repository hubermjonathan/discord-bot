package com.hubermjonathan.discord.common.models

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

abstract class Button(private val id: String, protected val featureContext: FeatureContext) : ListenerAdapter() {
    protected val jda = featureContext.jda
    private val logger = featureContext.logger

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
