package com.hubermjonathan.discord.house;

import com.hubermjonathan.discord.house.button.*;
import com.hubermjonathan.discord.house.command.BuildHouse;
import com.hubermjonathan.discord.house.command.EditRoom;
import com.hubermjonathan.discord.house.event.KickVisitors;
import com.hubermjonathan.discord.house.event.ManageRooms;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.Timer;

public class HouseBot {
    public static void run(String token) throws LoginException, InterruptedException {
        Cancel cancel = new Cancel(Constants.CANCEL);
        Confirm confirm = new Confirm(Constants.CONFIRM);
        Deny deny = new Deny(Constants.DENY);
        Kick kick = new Kick(Constants.KICK);
        Knock knock = new Knock(Constants.KNOCK);
        Lock lock = new Lock(Constants.LOCK);
        Poke poke = new Poke("poke");
        Unlock unlock = new Unlock(Constants.UNLOCK);

        BuildHouse build = new BuildHouse("build", "build the house");
        EditRoom room = new EditRoom("room", "edit the details of your room");

        ManageRooms manageRooms = new ManageRooms();

        JDA jda = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(
                        cancel, confirm, deny, kick, knock, lock, unlock,
                        poke,
                        build, room,
                        manageRooms
                )
                .build();

        jda.awaitReady();

        for (Guild guild : jda.getGuilds()) {
            guild.updateCommands().addCommands(
                    poke.getCommandData(),
                    build.getCommandData(), room.getCommandData()
            ).queue();

            Timer timer = new Timer();
            timer.schedule(new KickVisitors(guild), 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 24);
        }
    }
}
