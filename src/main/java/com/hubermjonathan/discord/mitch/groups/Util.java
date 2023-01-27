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

public class Util {
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
