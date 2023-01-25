package com.hubermjonathan.discord.mitch;

import com.hubermjonathan.discord.mitch.events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class MitchBot {
    public static void run(final String token) throws  InterruptedException {
        final ManageGroups manageGroups = new ManageGroups();
        final ManageTourGroup manageTourGroup = new ManageTourGroup();

        final JDA jda = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(manageGroups, manageTourGroup)
                .build();

        jda.awaitReady();
    }
}
