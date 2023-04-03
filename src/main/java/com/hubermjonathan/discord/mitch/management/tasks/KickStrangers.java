package com.hubermjonathan.discord.mitch.management.tasks;

import com.hubermjonathan.discord.common.models.Task;
import com.hubermjonathan.discord.mitch.Constants;
import com.hubermjonathan.discord.common.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class KickStrangers extends Task {
    public KickStrangers(Guild guild, long delay, long schedule) {
        super(guild, delay, schedule);
    }

    @Override
    public void execute() {
        Role strangersRole = getGuild()
                .getRoleById(Constants.STRANGERS_ROLE_ID);

        for (Member member : getGuild().getMembersWithRoles(strangersRole)) {
            Logger.log(
                    getGuild().getJDA(),
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
