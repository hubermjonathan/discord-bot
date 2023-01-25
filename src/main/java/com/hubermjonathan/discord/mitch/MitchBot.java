package com.hubermjonathan.discord.mitch;

import com.hubermjonathan.discord.mitch.events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.Timer;

public class MitchBot {
    public static void run(final String token) throws  InterruptedException {
        final ManageGroups manageGroups = new ManageGroups();
        final ManageStrangers manageStrangers = new ManageStrangers();

        final JDA jda = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(manageGroups, manageStrangers)
                .build();

        jda.awaitReady();

        for (Guild guild : jda.getGuilds()) {
            Timer timer = new Timer();

            timer.schedule(new KickStrangers(guild), 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 12);
        }
    }
}
