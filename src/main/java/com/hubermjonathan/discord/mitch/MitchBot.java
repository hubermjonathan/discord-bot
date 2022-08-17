package com.hubermjonathan.discord.mitch;

import com.hubermjonathan.discord.mitch.buttons.*;
import com.hubermjonathan.discord.mitch.commands.CreateEvent;
import com.hubermjonathan.discord.mitch.commands.EditEvent;
import com.hubermjonathan.discord.mitch.events.ManageMusicChannel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class MitchBot {
    public static void run(String token) throws LoginException, InterruptedException {
        final Delete delete = new Delete("delete");
        final Going going = new Going("going");
        final Maybe maybe = new Maybe("maybe");
        final NotGoing notGoing = new NotGoing("notGoing");

        final CreateEvent createEvent = new CreateEvent("event", "create an event");
        final EditEvent editEvent = new EditEvent("edit", "edit an event");

        final ManageMusicChannel manageMusicChannel = new ManageMusicChannel();

        final JDA jda = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(
                        delete, going, maybe, notGoing,
                        createEvent, editEvent,
                        manageMusicChannel
                )
                .build();

        jda.awaitReady();

        for (Guild guild : jda.getGuilds()) {
            guild.updateCommands().addCommands(
                    createEvent.getCommandData(),
                    editEvent.getCommandData()
            ).queue();
        }
    }
}
