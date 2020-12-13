package com.hubermjonathan.mitch;

import com.hubermjonathan.mitch.commands.*;
import com.hubermjonathan.mitch.events.*;
import com.hubermjonathan.mitch.tasks.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.Timer;

public class Mitch {
    public static void main(String[] args) throws InterruptedException, LoginException {
        JDA jda = JDABuilder.createDefault(System.getenv("TOKEN")).enableIntents(GatewayIntent.GUILD_MEMBERS).build();
        jda.awaitReady();

        jda.addEventListener(new ChangeRegion());
        jda.addEventListener(new ResetLeaderboards());
        jda.addEventListener(new ShowLeaderboards());
        jda.addEventListener(new TogglePriority());

        jda.addEventListener(new NewMemberActions());
        jda.addEventListener(new TrackConnectionTime());

        Timer timer = new Timer();
        timer.schedule(new UpdateStatus(jda.getPresence()), 0, 60 * 60 * 1000);
    }
}
