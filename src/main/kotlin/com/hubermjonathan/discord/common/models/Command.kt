package com.hubermjonathan.discord.common.models

import com.hubermjonathan.discord.mitch.Constants
import com.hubermjonathan.discord.mitch.MitchConfig
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class Command(private val name: String, val commandData: CommandData, protected val context: Context, private val allowedChannels: List<String>? = null) : ListenerAdapter(), KoinComponent {
    protected val jda: JDA by inject()
    private val logger = context.logger

    protected open fun shouldIgnoreEvent(event: SlashCommandInteractionEvent): Boolean {
        val userIsBot = event.user.isBot
        val commandNameIsWrong = event.name != this.name
        val isRunningInDevMode = MitchConfig.DEV_MODE
        val channelIsNotAllowed = allowedChannels?.contains(event.channel.id) == false
        val channelIsNotTestingChannel = event.channel.id != Constants.BOT_TESTING_CHANNEL_ID

        return userIsBot ||
            commandNameIsWrong ||
            (!isRunningInDevMode && channelIsNotAllowed) ||
            (isRunningInDevMode && channelIsNotTestingChannel)
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (shouldIgnoreEvent(event)) return

        try {
            execute(event)
            event
                .reply("nice") // TODO
                .setEphemeral(true)
                .queue()
        } catch (e: Exception) {
            logger.error(e.localizedMessage, e)
            event
                .reply(e.localizedMessage)
                .setEphemeral(true)
                .queue()
        }
    }

    @Throws(Exception::class)
    abstract fun execute(event: SlashCommandInteractionEvent)
}
