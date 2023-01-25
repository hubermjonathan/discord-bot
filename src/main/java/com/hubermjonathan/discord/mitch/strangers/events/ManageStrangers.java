package com.hubermjonathan.discord.mitch.strangers.events;

import com.hubermjonathan.discord.common.Constants;
import com.hubermjonathan.discord.mitch.MitchConstants;
import com.hubermjonathan.discord.mitch.MitchLogger;
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

        final Role strangersRole = event
                .getGuild()
                .getRolesByName(MitchConstants.STRANGERS_ROLE_NAME, true)
                .get(0);

        MitchLogger.log(
                event.getGuild().getMemberById(Constants.BOT_OWNER_ID).getUser(),
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
