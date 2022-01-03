package com.hubermjonathan.discordbot;

import com.hubermjonathan.discordbot.buttons.*;
import com.hubermjonathan.discordbot.commands.*;
import com.hubermjonathan.discordbot.events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.Timer;

public class DiscordBot {
    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA jda = JDABuilder.createDefault(System.getenv("TOKEN"))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();
        jda.awaitReady();

        jda.addEventListener(new Cancel(Constants.CANCEL, false));
        jda.addEventListener(new Confirm(Constants.CONFIRM));
        jda.addEventListener(new Deny(Constants.DENY));
        jda.addEventListener(new Kick(Constants.KICK));
        jda.addEventListener(new Knock(Constants.KNOCK, false));
        jda.addEventListener(new Lock(Constants.LOCK));
        jda.addEventListener(new Unlock(Constants.UNLOCK));

        jda.addEventListener(new CreateHouse("create"));
        jda.addEventListener(new EditRoom("room"));

        jda.addEventListener(new ManageHouse());

        Timer timer = new Timer();
        timer.schedule(new KickVisitors(jda.getGuilds().get(0)), 1000 * 60 * 60, 1000 * 60 * 60);
    }
}
