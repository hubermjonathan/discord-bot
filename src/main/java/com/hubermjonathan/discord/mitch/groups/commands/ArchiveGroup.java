package com.hubermjonathan.discord.mitch.groups.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import com.hubermjonathan.discord.common.models.Context;
import com.hubermjonathan.discord.mitch.Constants;
import com.hubermjonathan.discord.mitch.groups.Util;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArchiveGroup extends BotOwnerCommand {
    public ArchiveGroup(@NotNull Context context) {
        super(
                "archive",
                Commands
                        .slash("archive", "archive a group")
                        .addOption(OptionType.CHANNEL, "channel", "group channel to archive", true),
                context,
                List.of(Constants.SIGNUP_SHEET_TEXT_CHANNEL_ID)
        );
    }
    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) throws Exception {
        Category category = event
                .getGuild()
                .getCategoryById(Constants.ARCHIVED_GROUPS_CATEGORY_ID);

        event
                .getGuild()
                .getTextChannelById(event.getOption("channel").getAsChannel().getId())
                .getManager()
                .setParent(category)
                .sync()
                .queue();
        Util.updateGroupsMessage(event, List.of(event.getChannel().getId()));
    }
}
