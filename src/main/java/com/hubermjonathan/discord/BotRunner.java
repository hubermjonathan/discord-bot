package com.hubermjonathan.discord;

import com.hubermjonathan.discord.mitch.MitchBot;

public class BotRunner {
    public static void main(final String[] args) throws InterruptedException {
        if (System.getenv("MITCH_TOKEN") != null) {
            System.out.println("running mitch bot");
            MitchBot.run(System.getenv("MITCH_TOKEN"));
        }
    }
}
