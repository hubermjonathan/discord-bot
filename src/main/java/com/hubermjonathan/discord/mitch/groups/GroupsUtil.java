package com.hubermjonathan.discord.mitch.groups;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupsUtil {
    public static MessageData buildGroupMessage(
            @Nullable final Message message,
            final List<ActionRow> actionRows
    ) {
        if (message != null) {
            final MessageEditBuilder messageBuilder = new MessageEditBuilder();

            messageBuilder.setComponents(actionRows);

            return messageBuilder.build();
        } else {
            final MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
            final EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle("\uD83D\uDC65 groups");
            embedBuilder.setDescription("click the buttons below to join or leave a group");
            embedBuilder.setColor(0xcfb991);
            messageBuilder.setEmbeds(embedBuilder.build());
            messageBuilder.setComponents(actionRows);

            return messageBuilder.build();
        }
    }

    public static String getGroupName(final String groupChannelName) {
        final List<String> groupNameWords = new ArrayList<>(Arrays.asList(groupChannelName.split("-")));

        groupNameWords.remove(0);

        return String.join(" ", groupNameWords);
    }

    public static String getGroupEmoji(final String groupChannelName) {
        return groupChannelName.split("-")[0];
    }

    public static List<Button> getGroupButtons(final List<TextChannel> groupChannels) {
        final List<Button> buttons = new ArrayList<>();

        for (final TextChannel textChannel : groupChannels) {
            buttons.add(
                    Button.secondary(
                            textChannel.getId(),
                            getGroupEmoji(textChannel.getName()) + " " + getGroupName(textChannel.getName())
                    )
            );
        }

        return buttons;
    }

    public static List<ActionRow> getGroupActionRows(final List<Button> groupButtons) {
        final List<ActionRow> actionRows = new ArrayList<>();

        for (int i = 0; i < groupButtons.size(); i += 5) {
            actionRows.add(ActionRow.of(groupButtons.subList(i, Math.min(i + 5, groupButtons.size()))));
        }

        return actionRows;
    }
}
