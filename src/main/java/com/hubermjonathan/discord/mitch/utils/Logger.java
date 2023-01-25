package com.hubermjonathan.discord.mitch.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static void log(final User user, final String title, final String message) {
        final MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message);
        embedBuilder.setColor(0xcfb991);
        embedBuilder.setFooter(dateFormatter.format(new Date()));
        messageBuilder.setEmbeds(embedBuilder.build());
        user
                .openPrivateChannel()
                .complete()
                .sendMessage(messageBuilder.build())
                .queue();
    }
}
