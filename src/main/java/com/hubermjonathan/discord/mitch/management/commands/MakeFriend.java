package com.hubermjonathan.discord.mitch.management.commands;

import com.hubermjonathan.discord.common.Logger;
import com.hubermjonathan.discord.common.models.Command;
import com.hubermjonathan.discord.mitch.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class MakeFriend extends Command {
    public MakeFriend() {
        super(
                "friend",
                Commands
                        .slash("friend", "make a stranger a friend")
                        .addOption(OptionType.USER, "stranger", "the stranger to make a friend", true),
                null
        );
    }

    @Override
    public void execute() throws Exception {
        Member stranger = getEvent()
                .getGuild()
                .getMember(
                        getEvent()
                                .getOption("stranger")
                                .getAsUser()
                );
        Role strangersRole = getEvent()
                .getGuild()
                .getRoleById(Constants.STRANGERS_ROLE_ID);
        Role friendsRole = getEvent()
                .getGuild()
                .getRoleById(Constants.FRIENDS_ROLE_ID);

        if (!stranger.getRoles().contains(strangersRole) || stranger.getRoles().contains(friendsRole)) {
            throw new Exception();
        }

        getEvent()
                .getGuild()
                .addRoleToMember(stranger, friendsRole)
                .queue();
        getEvent()
                .getGuild()
                .removeRoleFromMember(stranger, strangersRole)
                .queue();
        Logger.log(
                getEvent().getJDA(),
                "\uD83D\uDC64 management",
                String.format(
                        "%s made %s a friend",
                        getEvent().getMember().getAsMention(),
                        stranger.getAsMention()
                )
        );
    }
}
