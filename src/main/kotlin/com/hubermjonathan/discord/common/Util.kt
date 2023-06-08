package com.hubermjonathan.discord.common

import net.dv8tion.jda.api.EmbedBuilder
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

private val dateFormatter = SimpleDateFormat("MM/dd/yyyy hh:mm aa").apply {
    timeZone = TimeZone.getTimeZone(ZoneId.of(ZoneId.SHORT_IDS["PST"]))
}

object Util {
    fun buildMessageEmbed(title: String, message: String, isError: Boolean = false): EmbedBuilder {
        return EmbedBuilder()
            .setTitle(title)
            .setDescription(message)
            .setColor(if (isError) 0xed4245 else 0xcfb991)
            .setFooter(dateFormatter.format(Date()))
    }
}
