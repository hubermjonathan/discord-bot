package com.hubermjonathan.mitch;


import com.hubermjonathan.mitch.welcome.WelcomeEvents;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Mitch {
    public static void main(String[] args) throws LoginException {
        JDA api = JDABuilder.createDefault(System.getenv("TOKEN")).enableIntents(GatewayIntent.GUILD_MEMBERS).build();
        api.addEventListener(new Dispatcher());
        api.addEventListener(new WelcomeEvents());
    }
}
