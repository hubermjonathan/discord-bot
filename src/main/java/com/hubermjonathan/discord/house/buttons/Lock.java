package com.hubermjonathan.discord.house.buttons;

import com.hubermjonathan.discord.house.Constants;
import com.hubermjonathan.discord.common.models.ResidentButton;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.EnumSet;

public class Lock extends ResidentButton {
    public Lock(String name) {
        super(
                name,
                Commands.user(name)
        );
    }

    @Override
    public String execute() throws Exception {
        getEvent().getGuild().getVoiceChannelsByName(Constants.getResidentRoomDefaultName(getEvent().getMember().getEffectiveName()), true).get(0).getManager()
                .clearOverridesAdded()
                .putPermissionOverride(getEvent().getGuild().getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .putMemberPermissionOverride(getEvent().getMember().getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                .queue();

        return "your room is now locked!" + Emoji.fromUnicode(Constants.LOCK).getName();
    }
}
