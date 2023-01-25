package com.hubermjonathan.discord.mitch.events;

import com.hubermjonathan.discord.mitch.MitchConstants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.TimerTask;

public class KickStrangers extends TimerTask {
    private final Guild guild;

    public KickStrangers(final Guild guild) {
        this.guild = guild;
    }

    @Override
    public void run() {
        Role strangersRole = guild.getRolesByName(MitchConstants.STRANGERS_ROLE_NAME, true).get(0);

        for (Member member : guild.getMembersWithRoles(strangersRole)) {
            member.kick().queue();
        }
    }
}
