package com.hubermjonathan.discord.mitch.emoji;

import com.hubermjonathan.discord.mitch.MitchConstants;
import com.hubermjonathan.discord.mitch.emoji.commands.UploadEmoji;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Timer;

public class Emoji {
    public static void load(final JDABuilder jda) {
    }

    public static void loadDeferred(final JDA jda) {
        final Guild guild = jda.getGuildById(MitchConstants.SERVER_ID);
        final Timer timer = new Timer();

        final UploadEmoji uploadEmoji = new UploadEmoji("emoji", "upload an emoji");

        guild
                .updateCommands()
                .addCommands(
                        uploadEmoji.getCommandData()
                )
                .queue();
    }
}
