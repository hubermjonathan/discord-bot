package com.hubermjonathan.discord.mitch.events;

import com.hubermjonathan.discord.mitch.MitchConstants;
import com.hubermjonathan.discord.mitch.utils.Logger;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ManageStrangers extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull final GuildMemberJoinEvent event) {
        if (event.getUser().isBot()) {
            return;
        }

        Role strangersRole = event
                .getGuild()
                .getRolesByName(MitchConstants.STRANGERS_ROLE_NAME, true)
                .get(0);

        Logger.log(
                event.getGuild().getMemberById(System.getenv("BOT_OWNER_ID")).getUser(),
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
