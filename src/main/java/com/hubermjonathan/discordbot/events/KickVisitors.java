package com.hubermjonathan.discordbot.events;

import com.hubermjonathan.discordbot.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.time.OffsetDateTime;
import java.util.TimerTask;

public class KickVisitors extends TimerTask {
    Guild guild;

    public KickVisitors(Guild guild) {
        this.guild = guild;
    }

    @Override
    public void run() {
        for (Member member : guild.loadMembers().get()) {
            for (Role role : member.getRoles()) {
                if (role.getName().equals(Constants.VISITOR_ROLE_NAME) && member.getTimeJoined().isBefore(OffsetDateTime.now().minusHours(12))) {
                    member.kick().queue();
                }
            }
        }
    }
}
