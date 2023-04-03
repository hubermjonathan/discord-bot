package com.hubermjonathan.discord;

import com.hubermjonathan.discord.mitch.MitchBot;

public class BotRunner {
    public static void main(final String[] args) throws InterruptedException {
        if (System.getenv("BOT_OWNER_ID") == null) {
            System.out.println("missing bot owner id");
            System.exit(1);
        }

        if (System.getenv("MITCH_TOKEN") != null) {
            MitchBot mitchBot = new MitchBot();

            System.out.println("running mitch bot");
            mitchBot.run(System.getenv("MITCH_TOKEN"));
        }
    }
}
