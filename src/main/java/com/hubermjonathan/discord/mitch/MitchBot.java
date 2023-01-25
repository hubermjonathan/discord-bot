package com.hubermjonathan.discord.mitch;

import com.hubermjonathan.discord.common.Constants;
import com.hubermjonathan.discord.mitch.emoji.Emoji;
import com.hubermjonathan.discord.mitch.events.Events;
import com.hubermjonathan.discord.mitch.groups.Groups;
import com.hubermjonathan.discord.mitch.music.Music;
import com.hubermjonathan.discord.mitch.strangers.Strangers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.ArrayList;
import java.util.List;

public class MitchBot {
    public static void run(final String token) throws InterruptedException {
        final List<String> enabledFeatures = new ArrayList<>();
        final List<String> disabledFeatures = new ArrayList<>();
        JDABuilder jdaBuilder = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS);

        if (System.getenv("MITCH_ENABLE_EMOJI") != null) {
            Emoji.load(jdaBuilder);
            enabledFeatures.add("emoji");
        } else {
            disabledFeatures.add("emoji");
        }

        if (System.getenv("MITCH_ENABLE_EVENTS") != null) {
            Events.load(jdaBuilder);
            enabledFeatures.add("events");
        } else {
            disabledFeatures.add("events");
        }

        if (System.getenv("MITCH_ENABLE_GROUPS") != null) {
            Groups.load(jdaBuilder);
            enabledFeatures.add("groups");
        } else {
            disabledFeatures.add("groups");
        }

        if (System.getenv("MITCH_ENABLE_MUSIC") != null) {
            Music.load(jdaBuilder);
            enabledFeatures.add("music");
        } else {
            disabledFeatures.add("music");
        }

        if (System.getenv("MITCH_ENABLE_STRANGERS") != null) {
            Strangers.load(jdaBuilder);
            enabledFeatures.add("strangers");
        } else {
            disabledFeatures.add("strangers");
        }

        final JDA jda = jdaBuilder
                .build()
                .awaitReady();

        if (System.getenv("MITCH_ENABLE_EMOJI") != null) {
            Emoji.loadDeferred(jda);
        }

        if (System.getenv("MITCH_ENABLE_EVENTS") != null) {
            Events.loadDeferred(jda);
        }

        if (System.getenv("MITCH_ENABLE_GROUPS") != null) {
            Groups.loadDeferred(jda);
        }

        if (System.getenv("MITCH_ENABLE_MUSIC") != null) {
            Music.loadDeferred(jda);
        }

        if (System.getenv("MITCH_ENABLE_STRANGERS") != null) {
            Strangers.loadDeferred(jda);
        }

        MitchLogger.log(
                jda.getUserById(Constants.BOT_OWNER_ID),
                "\uD83D\uDCC4 system",
                "bot started" + "\nenabled: " + String.join(", ", enabledFeatures) + "\ndisabled: " + String.join(", ", disabledFeatures)
        );
    }
}
