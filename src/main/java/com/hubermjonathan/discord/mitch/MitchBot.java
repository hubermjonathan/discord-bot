package com.hubermjonathan.discord.mitch;

import com.hubermjonathan.discord.common.Logger;
import com.hubermjonathan.discord.common.models.Command;
import com.hubermjonathan.discord.common.models.Feature;
import com.hubermjonathan.discord.mitch.draft.Draft;
import com.hubermjonathan.discord.mitch.management.Management;
import com.hubermjonathan.discord.mitch.events.Events;
import com.hubermjonathan.discord.mitch.groups.Groups;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MitchBot {
    private final List<Feature> features = new ArrayList<>();

    public MitchBot() {
        features.add(new Draft(true));
        features.add(new Events(false));
        features.add(new Groups(true));
        features.add(new Management(true));
    }

    public void run(String token) throws InterruptedException {
        JDABuilder jdaBuilder = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS);

        for (Feature feature : features) {
            feature.load(jdaBuilder);
        }

        JDA jda = jdaBuilder
                .build()
                .awaitReady();
        Guild guild = jda.getGuildById(Constants.SERVER_ID);
        List<CommandData> commands = features
                .stream()
                .map(Feature::getCommands)
                .flatMap(List::stream)
                .map(Command::getCommandData)
                .collect(Collectors.toList());

        guild
                .updateCommands()
                .addCommands(commands)
                .queue();

        for (Feature feature : features) {
            feature.startTasks(guild);
        }

        if (System.getenv("DEV") != null) {
            List<String> enabledFeatures = features
                    .stream()
                    .filter(Feature::isEnabled)
                    .map(feature -> feature.getClass().getSimpleName())
                    .collect(Collectors.toList());
            List<String> disabledFeatures = features
                    .stream()
                    .filter(feature -> !feature.isEnabled())
                    .map(feature -> feature.getClass().getSimpleName())
                    .collect(Collectors.toList());

            Logger.log(
                    jda,
                    "\uD83D\uDCC4 system",
                    "bot started" + "\nenabled features: " + String.join(", ", enabledFeatures) + "\ndisabled features: " + String.join(", ", disabledFeatures)
            );
        }
    }
}
