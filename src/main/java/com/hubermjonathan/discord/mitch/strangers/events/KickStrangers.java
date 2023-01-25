package com.hubermjonathan.discord.mitch.strangers.events;

import com.hubermjonathan.discord.common.Constants;
import com.hubermjonathan.discord.mitch.MitchConstants;
import com.hubermjonathan.discord.mitch.MitchLogger;
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
        final Role strangersRole = guild
                .getRolesByName(MitchConstants.STRANGERS_ROLE_NAME, true)
                .get(0);

        for (final Member member : guild.getMembersWithRoles(strangersRole)) {
            MitchLogger.log(
                    guild.getMemberById(Constants.BOT_OWNER_ID).getUser(),
                    "\uD83D\uDC64 strangers",
                    String.format(
                            "kicked %s",
                            member.getEffectiveName()
                    )
            );

            member
                    .kick()
                    .queue();
        }
    }
}
