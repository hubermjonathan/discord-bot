package com.hubermjonathan.discord.common.models

import com.hubermjonathan.discord.common.Util.buildMessageEmbed
import com.hubermjonathan.discord.mitch.MitchConfig
import com.hubermjonathan.discord.mitch.botTestingChannel
import com.hubermjonathan.discord.mitch.purdudesGuild
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData

abstract class Command(private val name: String, val commandData: CommandData, protected val featureContext: FeatureContext) : ListenerAdapter() {
    protected open val allowedChannels: List<TextChannel>? = null
    protected val jda = featureContext.jda
    private val logger = featureContext.logger

    protected open fun shouldIgnoreEvent(event: SlashCommandInteractionEvent): Boolean {
        val userIsBot = event.user.isBot
        val commandNameIsWrong = event.name != this.name
        val channelIsNotTextChannel = event.channel !is TextChannel

        return userIsBot ||
            commandNameIsWrong ||
            channelIsNotTextChannel
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (shouldIgnoreEvent(event)) return

        try {
            val isRunningInDevMode = MitchConfig.DEV_MODE
            val channelIsNotAllowed = allowedChannels?.contains(event.channel.asTextChannel()) == false
            val channelIsNotTestingChannel = event.channel != jda.purdudesGuild.botTestingChannel

            if (!isRunningInDevMode && channelIsNotAllowed) {
                throw InteractionException(
                    "command cannot be run in this channel",
                    event.user,
                    name,
                    featureContext,
                )
            }

            if (isRunningInDevMode && channelIsNotTestingChannel) {
                throw InteractionException(
                    "command cannot be run outside of bot testing while in dev mode",
                    event.user,
                    name,
                    featureContext,
                )
            }

            val result = execute(event)

            event
                .replyEmbeds(buildMessageEmbed("\uD83E\uDD18 $name", result).build())
                .setEphemeral(true)
                .queue()
        } catch (e: InteractionException) {
            logger.error(e.localizedMessage, e)
            event
                .replyEmbeds(buildMessageEmbed("\u26D4 $name", e.localizedMessage, true).build())
                .setEphemeral(true)
                .queue()
        }
    }

    abstract fun execute(event: SlashCommandInteractionEvent): String
}
