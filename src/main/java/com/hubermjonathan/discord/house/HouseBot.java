package com.hubermjonathan.discord.house;

import com.hubermjonathan.discord.house.button.*;
import com.hubermjonathan.discord.house.command.BuildHouse;
import com.hubermjonathan.discord.house.event.KickVisitors;
import com.hubermjonathan.discord.house.event.ManageRooms;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.Timer;

public class HouseBot {
    public static void run(String token) throws LoginException, InterruptedException {
        Lock lock = new Lock( Emoji.fromUnicode(Constants.LOCK).getName() + " lock");
        Poke poke = new Poke( Emoji.fromUnicode(Constants.POKE).getName() + " poke");
        Unlock unlock = new Unlock( Emoji.fromUnicode(Constants.UNLOCK).getName() + " unlock");

        BuildHouse build = new BuildHouse("build", "build the house");

        ManageRooms manageRooms = new ManageRooms();

        JDA jda = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(
                        lock, poke, unlock,
                        build,
                        manageRooms
                )
                .build();

        jda.awaitReady();

        for (Guild guild : jda.getGuilds()) {
            guild.updateCommands().addCommands(
                    lock.getCommandData(),
                    poke.getCommandData(),
                    unlock.getCommandData(),
                    build.getCommandData()
            ).queue();

            Timer timer = new Timer();
            timer.schedule(new KickVisitors(guild), 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 12);
        }
    }
}
