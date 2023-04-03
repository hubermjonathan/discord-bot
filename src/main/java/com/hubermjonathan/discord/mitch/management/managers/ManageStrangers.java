package com.hubermjonathan.discord.mitch.management.managers;

import com.hubermjonathan.discord.common.models.Manager;
import com.hubermjonathan.discord.mitch.Constants;
import com.hubermjonathan.discord.common.Logger;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.jetbrains.annotations.NotNull;

public class ManageStrangers extends Manager {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (event.getUser().isBot()) {
            return;
        }

        Role strangersRole = event
                .getGuild()
                .getRoleById(Constants.STRANGERS_ROLE_ID);

        Logger.log(
                event.getJDA(),
                "\uD83D\uDC64 strangers",
                String.format(
                        "%s joined",
                        event.getMember().getEffectiveName()
                )
        );
        event
                .getGuild()
                .addRoleToMember(event.getMember(), strangersRole)
                .queue();
    }
}
