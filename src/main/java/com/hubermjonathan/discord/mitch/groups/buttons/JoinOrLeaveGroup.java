package com.hubermjonathan.discord.mitch.groups.buttons;

import com.hubermjonathan.discord.common.models.Button;
import com.hubermjonathan.discord.common.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.EnumSet;
import java.util.List;

public class JoinOrLeaveGroup extends Button {
    private final String groupChannelId;

    public JoinOrLeaveGroup(String groupChannelId) {
        super(groupChannelId);

        this.groupChannelId = groupChannelId;
    }

    @Override
    public void execute() throws Exception {
        TextChannel groupChannel = getEvent()
                .getGuild()
                .getTextChannelById(groupChannelId);
        List<PermissionOverride> memberPermissionOverrides = groupChannel.getMemberPermissionOverrides();

        for (PermissionOverride memberPermissionOverride : memberPermissionOverrides) {
            if (memberPermissionOverride.getMember().equals(getEvent().getMember())) {
                groupChannel
                        .getManager()
                        .removePermissionOverride(memberPermissionOverride.getPermissionHolder())
                        .queue();
                Logger.log(
                        getEvent().getJDA(),
                        "\uD83D\uDC65 groups",
                        String.format(
                                "%s left #%s",
                                getEvent().getMember().getEffectiveName(),
                                groupChannel.getName()
                        )
                );

                return;
            }
        }

        groupChannel
                .getManager()
                .putMemberPermissionOverride(getEvent().getMember().getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                .queue();
        Logger.log(
                getEvent().getJDA(),
                "\uD83D\uDC65 groups",
                String.format(
                        "%s joined #%s",
                        getEvent().getMember().getEffectiveName(),
                        groupChannel.getName()
                )
        );
    }
}