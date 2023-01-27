package com.hubermjonathan.discord.mitch.groups.managers;

import com.hubermjonathan.discord.common.models.Manager;
import com.hubermjonathan.discord.mitch.Constants;
import com.hubermjonathan.discord.mitch.groups.Util;
import com.hubermjonathan.discord.mitch.groups.buttons.JoinOrLeaveGroup;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdatePositionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ManageGroups extends Manager {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        addEventListeners(event);
        updateGroupsMessage(event);
    }

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {
        event.getJDA().addEventListener(
                new JoinOrLeaveGroup(event.getChannel().asTextChannel().getId()));

        updateGroupsMessage(event);
    }

    @Override
    public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
        updateGroupsMessage(event);
    }

    @Override
    public void onChannelUpdateName(@NotNull ChannelUpdateNameEvent event) {
        updateGroupsMessage(event);
    }

    @Override
    public void onChannelUpdatePosition(@NotNull ChannelUpdatePositionEvent event) {
        updateGroupsMessage(event);
    }

    private void addEventListeners(@NotNull Event event) {
        Guild guild = event.getJDA().getGuildById(Constants.SERVER_ID);
        List<TextChannel> groupChannels = new ArrayList<>(
                guild
                        .getCategoriesByName(Constants.PUBLIC_GROUPS_CATEGORY_NAME, true)
                        .get(0)
                        .getTextChannels()
        );

        groupChannels.remove(0);

        for (TextChannel textChannel : groupChannels) {
            event
                    .getJDA()
                    .addEventListener(
                            new JoinOrLeaveGroup(textChannel.getId())
                    );
        }
    }

    private void updateGroupsMessage(@NotNull Event event) {
        Guild guild = event.getJDA().getGuildById(Constants.SERVER_ID);
        List<TextChannel> groupChannels = new ArrayList<>(
                guild.getCategoriesByName(Constants.PUBLIC_GROUPS_CATEGORY_NAME, true)
                        .get(0)
                        .getTextChannels()
        );
        TextChannel groupSelectionChannel = groupChannels.get(0);

        groupChannels.remove(0);

        List<Button> groupButtons = Util.getGroupButtons(groupChannels);
        List<ActionRow> groupActionRows = Util.getGroupActionRows(groupButtons);

        try {
            Message groupSelectionMessage = groupSelectionChannel
                    .retrieveMessageById(groupSelectionChannel.getLatestMessageId())
                    .complete();

            groupSelectionMessage
                    .editMessage((MessageEditData) Util.buildGroupMessage(groupSelectionMessage, groupActionRows))
                    .queue();
        } catch (Exception e) {
            groupSelectionChannel
                    .sendMessage((MessageCreateData) Util.buildGroupMessage(null, groupActionRows))
                    .queue();
        }
    }
}
