package com.hubermjonathan.discord.mitch.groups.events;

import com.hubermjonathan.discord.mitch.MitchConstants;
import com.hubermjonathan.discord.mitch.groups.GroupsUtil;
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
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ManageGroups extends ListenerAdapter {
    @Override
    public void onReady(@NotNull final ReadyEvent event) {
        addEventListeners(event);
        updateGroupsMessage(event);
    }

    @Override
    public void onChannelCreate(@NotNull final ChannelCreateEvent event) {
        event.getJDA().addEventListener(
                new JoinOrLeaveGroup(event.getChannel().asTextChannel().getId()));

        updateGroupsMessage(event);
    }

    @Override
    public void onChannelDelete(@NotNull final ChannelDeleteEvent event) {
        updateGroupsMessage(event);
    }

    @Override
    public void onChannelUpdateName(@NotNull final ChannelUpdateNameEvent event) {
        updateGroupsMessage(event);
    }

    @Override
    public void onChannelUpdatePosition(@NotNull final ChannelUpdatePositionEvent event) {
        updateGroupsMessage(event);
    }

    private void addEventListeners(@NotNull final Event event) {
        final Guild guild = event.getJDA().getGuildById(MitchConstants.SERVER_ID);
        final List<TextChannel> groupChannels = new ArrayList<>(
                guild
                        .getCategoriesByName(MitchConstants.PUBLIC_GROUPS_CATEGORY_NAME, true)
                        .get(0)
                        .getTextChannels()
        );

        groupChannels.remove(0);

        for (final TextChannel textChannel : groupChannels) {
            event
                    .getJDA()
                    .addEventListener(
                            new JoinOrLeaveGroup(textChannel.getId())
                    );
        }
    }

    private void updateGroupsMessage(@NotNull final Event event) {
        final Guild guild = event.getJDA().getGuildById(MitchConstants.SERVER_ID);
        final List<TextChannel> groupChannels = new ArrayList<>(
                guild.getCategoriesByName(MitchConstants.PUBLIC_GROUPS_CATEGORY_NAME, true)
                        .get(0)
                        .getTextChannels()
        );
        final TextChannel groupSelectionChannel = groupChannels.get(0);

        groupChannels.remove(0);

        final List<Button> groupButtons = GroupsUtil.getGroupButtons(groupChannels);
        final List<ActionRow> groupActionRows = GroupsUtil.getGroupActionRows(groupButtons);

        try {
            final Message groupSelectionMessage = groupSelectionChannel
                    .retrieveMessageById(groupSelectionChannel.getLatestMessageId())
                    .complete();

            groupSelectionMessage
                    .editMessage((MessageEditData) GroupsUtil.buildGroupMessage(groupSelectionMessage, groupActionRows))
                    .queue();
        } catch (final Exception e) {
            groupSelectionChannel
                    .sendMessage((MessageCreateData) GroupsUtil.buildGroupMessage(null, groupActionRows))
                    .queue();
        }
    }
}
