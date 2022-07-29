package com.hubermjonathan.discord.house.events;

import com.hubermjonathan.discord.house.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.TimerTask;

public class KickVisitors extends TimerTask {
    private Guild guild;

    public KickVisitors(Guild guild) {
        this.guild = guild;
    }

    @Override
    public void run() {
        Role visitorRole = guild.getRolesByName(Constants.VISITOR_ROLE_NAME, true).get(0);
        for (Member member : guild.getMembersWithRoles(visitorRole)) {
            member.kick().queue();
        }
    }
}
