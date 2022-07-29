package com.hubermjonathan.discord.mitch;

import com.hubermjonathan.discord.mitch.events.ManageMusicChannel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class MitchBot {
    public static void run(String token) throws LoginException, InterruptedException {
        ManageMusicChannel manageMusicChannel = new ManageMusicChannel();

        JDA jda = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(
                        manageMusicChannel
                )
                .build();

        jda.awaitReady();
    }
}
