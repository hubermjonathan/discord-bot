package com.hubermjonathan.discord.common

import com.hubermjonathan.discord.mitch.MitchConfig
import com.hubermjonathan.discord.mitch.botOwner
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

private val dateFormatter = SimpleDateFormat("MM/dd/yyyy hh:mm aa").apply {
    timeZone = TimeZone.getTimeZone(ZoneId.of(ZoneId.SHORT_IDS["PST"]))
}

class DiscordLogger(private val title: String, private val icon: String? = null) : KoinComponent {
    private val jda: JDA by inject()
    private val logger = LoggerFactory.getLogger(title)

    private enum class Severity {
        INFO,
        ERROR,
    }

    fun info(message: String) {
        this.log(Severity.INFO, "$icon $title", message)
    }

    fun error(message: String, exception: Throwable? = null) {
        this.log(Severity.ERROR, "\u26D4 error", message, exception)
    }

    private fun log(severity: Severity, title: String, message: String, exception: Throwable? = null) {
        when (severity) {
            Severity.INFO -> logger.info(message)
            Severity.ERROR -> logger.error(message, exception)
        }

        if (MitchConfig.LOG_EVENTS_TO_BOT_OWNER) {
            messageBotOwner(title, message)
        }
    }

    private fun messageBotOwner(title: String, message: String) {
        val embed = EmbedBuilder()
            .setTitle(title)
            .setDescription(message)
            .setColor(0xcfb991)
            .setFooter(dateFormatter.format(Date()))
            .build()

        jda.botOwner
            .openPrivateChannel()
            .complete()
            .sendMessage(MessageCreateData.fromEmbeds(embed))
            .queue()
    }
}
