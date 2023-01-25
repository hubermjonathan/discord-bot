package com.hubermjonathan.discord.mitch.buttons;

import com.hubermjonathan.discord.common.models.Button;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.PermissionOverride;

import java.util.EnumSet;
import java.util.List;

public class JoinOrLeaveGroup extends Button {
    private final String groupChannelId;

    public JoinOrLeaveGroup(final String groupChannelId) {
        super(groupChannelId);

        this.groupChannelId = groupChannelId;
    }

    @Override
    public void execute() throws Exception {
        final List<PermissionOverride> memberPermissionOverrides = getEvent().getGuild().getTextChannelById(groupChannelId).getMemberPermissionOverrides();

        for (PermissionOverride memberPermissionOverride : memberPermissionOverrides) {
            if (memberPermissionOverride.getMember().equals(getEvent().getMember())) {
                getEvent().getGuild().getTextChannelById(groupChannelId).getManager().removePermissionOverride(memberPermissionOverride.getPermissionHolder())
                        .queue();

                return;
            }
        }

        getEvent().getGuild().getTextChannelById(groupChannelId).getManager().putMemberPermissionOverride(getEvent().getMember().getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                .queue();
    }
}
