package com.hubermjonathan.discord.mitch.events;

import com.hubermjonathan.discord.mitch.MitchConstants;
import com.hubermjonathan.discord.mitch.buttons.JoinOrLeaveGroup;
import net.dv8tion.jda.api.EmbedBuilder;
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
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
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
        for (Guild guild : event.getJDA().getGuilds()) {
            final List<TextChannel> groupChannels = new ArrayList<>(
                    guild
                            .getCategoriesByName(MitchConstants.PUBLIC_GROUPS_CATEGORY_NAME, true)
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
    }

    private void updateGroupsMessage(@NotNull final Event event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            final List<TextChannel> groupChannels = new ArrayList<>(
                    guild.getCategoriesByName(MitchConstants.PUBLIC_GROUPS_CATEGORY_NAME, true)
                            .get(0)
                            .getTextChannels()
            );
            final TextChannel groupSelectionChannel = groupChannels.get(0);

            groupChannels.remove(0);

            final List<Button> groupButtons = getGroupButtons(groupChannels);
            final List<ActionRow> groupActionRows = getGroupActionRows(groupButtons);

            try {
                final Message groupSelectionMessage = groupSelectionChannel
                        .retrieveMessageById(groupSelectionChannel.getLatestMessageId())
                        .complete();
                final MessageEditBuilder messageBuilder = new MessageEditBuilder();

                messageBuilder.setComponents(groupActionRows);
                groupSelectionMessage
                        .editMessage(messageBuilder.build())
                        .queue();
            } catch (Exception e) {
                final MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
                final EmbedBuilder embedBuilder = new EmbedBuilder();

                embedBuilder.setTitle("\uD83D\uDC65 groups");
                embedBuilder.setDescription("click the buttons below to join or leave a group");
                embedBuilder.setColor(0xcfb991);
                messageBuilder.setEmbeds(embedBuilder.build());
                messageBuilder.setComponents(groupActionRows);
                groupSelectionChannel
                        .sendMessage(messageBuilder.build())
                        .queue();
            }
        }
    }

    private String getGroupName(final String groupChannelName) {
        final List<String> groupNameWords = new ArrayList<>(Arrays.asList(groupChannelName.split("-")));

        groupNameWords.remove(0);

        return String.join(" ", groupNameWords);
    }

    private String getGroupEmoji(final String groupChannelName) {
        return groupChannelName.split("-")[0];
    }

    private List<Button> getGroupButtons(final List<TextChannel> groupChannels) {
        final List<Button> buttons = new ArrayList<>();

        for (TextChannel textChannel : groupChannels) {
            buttons.add(
                    Button.secondary(
                            textChannel.getId(),
                            getGroupEmoji(textChannel.getName()) + " " + getGroupName(textChannel.getName())
                    )
            );
        }

        return buttons;
    }

    private List<ActionRow> getGroupActionRows(final List<Button> groupButtons) {
        final List<ActionRow> actionRows = new ArrayList<>();

        for (int i = 0; i < groupButtons.size(); i += 5) {
            actionRows.add(ActionRow.of(groupButtons.subList(i, Math.min(i + 5, groupButtons.size()))));
        }

        return actionRows;
    }
}
