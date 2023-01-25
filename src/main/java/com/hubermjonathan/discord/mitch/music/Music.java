package com.hubermjonathan.discord.mitch.music;

import com.hubermjonathan.discord.mitch.MitchConstants;
import com.hubermjonathan.discord.mitch.music.events.ManageMusicChannel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Timer;

public class Music {
    public static void load(final JDABuilder jda) {
        final ManageMusicChannel manageMusicChannel = new ManageMusicChannel();

        jda.addEventListeners(manageMusicChannel);
    }

    public static void loadDeferred(final JDA jda) {
        final Guild guild = jda.getGuildById(MitchConstants.SERVER_ID);
        final Timer timer = new Timer();
    }
}
