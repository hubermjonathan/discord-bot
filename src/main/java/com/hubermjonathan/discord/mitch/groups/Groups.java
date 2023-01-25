package com.hubermjonathan.discord.mitch.groups;

import com.hubermjonathan.discord.mitch.MitchConstants;
import com.hubermjonathan.discord.mitch.groups.events.ManageGroups;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Timer;

public class Groups {
    public static void load(final JDABuilder jda) {
        final ManageGroups manageGroups = new ManageGroups();

        jda.addEventListeners(manageGroups);
    }

    public static void loadDeferred(final JDA jda) {
        final Guild guild = jda.getGuildById(MitchConstants.SERVER_ID);
        final Timer timer = new Timer();
    }
}
