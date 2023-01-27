package com.hubermjonathan.discord.mitch;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class Logger {
    public static void log(User user, String title, String message) {
        MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");

        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message);
        embedBuilder.setColor(0xcfb991);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))));
        embedBuilder.setFooter(dateFormatter.format(new Date()));
        messageBuilder.setEmbeds(embedBuilder.build());
        user
                .openPrivateChannel()
                .complete()
                .sendMessage(messageBuilder.build())
                .queue();
    }
}
