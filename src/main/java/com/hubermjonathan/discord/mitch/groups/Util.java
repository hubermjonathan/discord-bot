package com.hubermjonathan.discord.mitch.groups;

import com.hubermjonathan.discord.mitch.Constants;
import com.hubermjonathan.discord.mitch.groups.buttons.JoinOrLeaveGroup;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Util {
    public static void addEventListeners(@NotNull Event event) {
        Guild guild = event.getJDA().getGuildById(Constants.SERVER_ID);
        List<TextChannel> groupChannels = new ArrayList<>(
                guild
                        .getCategoryById(Constants.PUBLIC_GROUPS_CATEGORY_ID)
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

    public static void updateGroupsMessage(@NotNull Event event) {
        updateGroupsMessage(event, new ArrayList<>());
    }

    public static void updateGroupsMessage(@NotNull Event event, List<String> excludedChannels) {
        Guild guild = event.getJDA().getGuildById(Constants.SERVER_ID);
        List<TextChannel> groupChannels = guild.getCategoryById(Constants.PUBLIC_GROUPS_CATEGORY_ID)
                .getTextChannels()
                .stream()
                .filter(textChannel -> !excludedChannels.contains(textChannel.getId()))
                .collect(Collectors.toList());
        TextChannel groupSelectionChannel = groupChannels.get(0);

        groupChannels.remove(0);

        List<Button> groupButtons = Util.getGroupButtons(groupChannels);
        List<ActionRow> groupActionRows = Util.getGroupActionRows(groupButtons);

        try {
            Message groupSelectionMessage = groupSelectionChannel
                    .retrieveMessageById(groupSelectionChannel.getLatestMessageId())
                    .complete();

            groupSelectionMessage
                    .editMessage((MessageEditData) buildGroupMessage(groupSelectionMessage, groupActionRows))
                    .queue();
        } catch (Exception e) {
            // FIXME
//            DiscordLogger.log(
//                    event.getJDA(),
//                    "\u26D4 error",
//                    String.format("%s", e.getLocalizedMessage())
//            );
            groupSelectionChannel
                    .sendMessage((MessageCreateData) buildGroupMessage(null, groupActionRows))
                    .queue();
        }
    }

    public static MessageData buildGroupMessage(
            @Nullable Message message,
            List<ActionRow> actionRows
    ) {
        if (message != null) {
            MessageEditBuilder messageBuilder = new MessageEditBuilder();

            messageBuilder.setComponents(actionRows);

            return messageBuilder.build();
        } else {
            MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("\uD83D\uDC65 groups");
            embedBuilder.setDescription("click the buttons below to join or leave a group");
            embedBuilder.setColor(0xcfb991);
            messageBuilder.setEmbeds(embedBuilder.build());
            messageBuilder.setComponents(actionRows);

            return messageBuilder.build();
        }
    }

    public static String getGroupName(String groupChannelName) {
        List<String> groupNameWords = new ArrayList<>(Arrays.asList(groupChannelName.split("-")));

        groupNameWords.remove(0);

        return String.join(" ", groupNameWords);
    }

    public static String getGroupEmoji(String groupChannelName) {
        return groupChannelName.split("-")[0];
    }

    public static List<Button> getGroupButtons(List<TextChannel> groupChannels) {
        List<Button> buttons = new ArrayList<>();

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

    public static List<ActionRow> getGroupActionRows(List<Button> groupButtons) {
        List<ActionRow> actionRows = new ArrayList<>();

        for (int i = 0; i < groupButtons.size(); i += 5) {
            actionRows.add(ActionRow.of(groupButtons.subList(i, Math.min(i + 5, groupButtons.size()))));
        }

        return actionRows;
    }
}
