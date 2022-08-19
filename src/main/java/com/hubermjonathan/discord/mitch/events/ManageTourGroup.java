package com.hubermjonathan.discord.mitch.events;

import com.hubermjonathan.discord.mitch.Constants;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ManageTourGroup extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull final GuildMemberJoinEvent event) {
        if (event.getUser().isBot()) {
            return;
        }

        Role tourGroupRole = event.getGuild().getRolesByName(Constants.TOUR_GROUP_ROLE_NAME, true).get(0);
        event.getGuild().addRoleToMember(event.getMember(), tourGroupRole).queue();
    }
}
