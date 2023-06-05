package com.hubermjonathan.discord.mitch.groups.managers;

import com.hubermjonathan.discord.common.models.Context;
import com.hubermjonathan.discord.common.models.Manager;
import com.hubermjonathan.discord.mitch.groups.Util;
import com.hubermjonathan.discord.mitch.groups.buttons.JoinOrLeaveGroup;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import org.jetbrains.annotations.NotNull;

public class ManageGroups extends Manager {
    public ManageGroups(@NotNull Context context) {
        super(context);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Util.addEventListeners(event);
        Util.updateGroupsMessage(event);
    }

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {
        if (!event.isFromType(ChannelType.TEXT)) {
            return;
        }

        event.getJDA().addEventListener(
                new JoinOrLeaveGroup(event.getChannel().asTextChannel().getId()));

        Util.updateGroupsMessage(event);
    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        if (!event.isFromType(ChannelType.TEXT)) {
            return;
        }

        Util.updateGroupsMessage(event);
    }

    @Override
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent event) {
        if (!event.isFromType(ChannelType.TEXT)) {
            return;
        }

        Util.updateGroupsMessage(event);
    }
}
