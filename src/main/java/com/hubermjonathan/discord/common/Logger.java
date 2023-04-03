package com.hubermjonathan.discord.common;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class Logger {
    public static void log(JDA jda, String title, String message) {
        if (System.getenv("LOG_EVENTS") != null) {
            MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");

            embedBuilder.setTitle(title);
            embedBuilder.setDescription(message);
            dateFormatter.setTimeZone(TimeZone.getTimeZone(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))));
            embedBuilder.setFooter(dateFormatter.format(new Date()));
            messageBuilder.setEmbeds(embedBuilder.build());
            jda.getUserById(System.getenv("BOT_OWNER_ID"))
                    .openPrivateChannel()
                    .complete()
                    .sendMessage(messageBuilder.build())
                    .queue();
        }
    }
}
