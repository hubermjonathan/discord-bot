package com.hubermjonathan.discord;

import com.hubermjonathan.discord.mitch.MitchBot;

import javax.security.auth.login.LoginException;

public class BotRunner {
    public static void main(final String[] args) throws LoginException, InterruptedException {
        if (System.getenv("BOT_OWNER_ID") == null) {
            System.out.println("missing environment variable: BOT_OWNER_ID");
            return;
        }


        if (System.getenv("MITCH_TOKEN") != null) {
            System.out.println("running mitch bot");
            MitchBot.run(System.getenv("MITCH_TOKEN"));
        }
    }
}
