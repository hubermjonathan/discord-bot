package com.hubermjonathan.discord.common

import com.hubermjonathan.discord.common.Util.buildMessageEmbed
import com.hubermjonathan.discord.common.models.InteractionException
import com.hubermjonathan.discord.mitch.MitchConfig
import com.hubermjonathan.discord.mitch.botOwner
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import org.slf4j.LoggerFactory

class DiscordLogger(private val jda: JDA, private val title: String, private val icon: String? = null) {
    private val logger = LoggerFactory.getLogger(title)

    private enum class Severity {
        INFO,
        ERROR,
    }

    fun info(message: String) {
        this.log(Severity.INFO, "$icon $title", message)
    }

    fun error(message: String, exception: InteractionException) {
        this.log(Severity.ERROR, "\u26D4 error", message, exception)
    }

    private fun log(severity: Severity, title: String, message: String, exception: InteractionException? = null) {
        when (severity) {
            Severity.INFO -> logger.info(message)
            Severity.ERROR -> logger.error(message, exception)
        }

        if (MitchConfig.LOG_EVENTS_TO_BOT_OWNER) {
            var embed = buildMessageEmbed(title, message, severity == Severity.ERROR)

            if (exception != null) {
                val metadata = "```" +
                    "feature: ${exception.featureContext.icon} ${exception.featureContext.name}\n" +
                    "interaction: ${exception.interactionName}\n" +
                    "caller: ${exception.user.name}\n" +
                    "```"

                embed = embed.addField("metadata", metadata, true)
            }

            jda.botOwner
                .openPrivateChannel()
                .complete()
                .sendMessage(MessageCreateData.fromEmbeds(embed.build()))
                .queue()
        }
    }
}
