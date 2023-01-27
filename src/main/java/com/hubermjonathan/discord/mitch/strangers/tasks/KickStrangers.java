package com.hubermjonathan.discord.mitch.strangers.tasks;

import com.hubermjonathan.discord.common.models.Task;
import com.hubermjonathan.discord.mitch.Constants;
import com.hubermjonathan.discord.mitch.Logger;
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
                .getRolesByName(Constants.STRANGERS_ROLE_NAME, true)
                .get(0);

        for (Member member : getGuild().getMembersWithRoles(strangersRole)) {
            Logger.log(
                    getGuild().getMemberById(Constants.BOT_OWNER_ID).getUser(),
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
