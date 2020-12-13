package com.hubermjonathan.mitch.events;

import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class NewMemberActions extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        if (event.getUser().isBot()) return;

        Member member = event.getMember();
        Guild guild = member.getGuild();
        Role defaultRole = guild.getRoleById(Constants.DEFAULT_ROLE_ID);

        member.modifyNickname("???'s Friend").queue();
        guild.addRoleToMember(member, defaultRole).queue();
    }
}
