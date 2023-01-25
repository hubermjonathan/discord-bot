package com.hubermjonathan.discord.mitch.strangers;

import com.hubermjonathan.discord.mitch.MitchConstants;
import com.hubermjonathan.discord.mitch.strangers.events.KickStrangers;
import com.hubermjonathan.discord.mitch.strangers.events.ManageStrangers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Timer;

public class Strangers {
    public static void load(final JDABuilder jda) {
        final ManageStrangers manageStrangers = new ManageStrangers();

        jda.addEventListeners(manageStrangers);
    }

    public static void loadDeferred(final JDA jda) {
        final Guild guild = jda.getGuildById(MitchConstants.SERVER_ID);
        final Timer timer = new Timer();

        timer.schedule(new KickStrangers(guild), 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 12);
    }
}
