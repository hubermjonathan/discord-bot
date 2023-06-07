package com.hubermjonathan.discord.common.models

import com.hubermjonathan.discord.mitch.botOwner
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData

abstract class BotOwnerCommand(name: String, commandData: CommandData, context: Context, allowedChannels: List<String>? = null) : Command(name, commandData, context, allowedChannels) {
    override fun shouldIgnoreEvent(event: SlashCommandInteractionEvent): Boolean {
        val userIsNotBotOwner = event.user != jda.botOwner

        return super.shouldIgnoreEvent(event) ||
            userIsNotBotOwner
    }
}
