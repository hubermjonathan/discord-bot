package com.hubermjonathan.discord.common.models

import com.hubermjonathan.discord.mitch.botOwner
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

abstract class BotOwnerButton(id: String, context: Context) : Button(id, context) {
    override fun shouldIgnoreEvent(event: ButtonInteractionEvent): Boolean {
        val userIsNotBotOwner = event.user != jda.botOwner

        return super.shouldIgnoreEvent(event) ||
            userIsNotBotOwner
    }
}
