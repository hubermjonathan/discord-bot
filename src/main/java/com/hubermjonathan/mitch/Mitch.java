package com.hubermjonathan.mitch;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Mitch {
    public static void main(String[] args) throws LoginException {
        JDA api = JDABuilder.createDefault(System.getenv("TOKEN")).build();
        api.addEventListener(new CommandHandler());
    }
}
