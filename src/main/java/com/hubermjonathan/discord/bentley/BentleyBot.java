package com.hubermjonathan.discord.bentley;

import com.hubermjonathan.discord.bentley.commands.UploadEmoji;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class BentleyBot {
    public static void run(String token) throws LoginException, InterruptedException {
        UploadEmoji upload = new UploadEmoji("upload", "upload an emoji");

        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(
                        upload
                )
                .build();

        jda.awaitReady();

        for (Guild guild : jda.getGuilds()) {
            guild.updateCommands().addCommands(
                    upload.getCommandData()
            ).queue();
        }
    }
}
