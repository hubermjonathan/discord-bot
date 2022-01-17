package com.hubermjonathan.discord.runner;

import com.hubermjonathan.discord.house.HouseBot;

import javax.security.auth.login.LoginException;

public class BotRunner {
    public static void main(String[] args) throws LoginException, InterruptedException {
        if (System.getenv("BOT_OWNER_ID") == null) {
            System.out.println("Missing environment variable: BOT_OWNER_ID");
            return;
        }

        HouseBot.run(System.getenv("HOUSE_TOKEN"));
    }
}