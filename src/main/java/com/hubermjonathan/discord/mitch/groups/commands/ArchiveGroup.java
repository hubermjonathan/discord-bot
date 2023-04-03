package com.hubermjonathan.discord.mitch.groups.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import com.hubermjonathan.discord.mitch.Constants;
import com.hubermjonathan.discord.mitch.groups.Util;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Arrays;

public class ArchiveGroup extends BotOwnerCommand {
    public ArchiveGroup() {
        super(
                "archive",
                Commands
                        .slash("archive", "archive a group")
                        .addOption(OptionType.CHANNEL, "channel", "group channel to archive"),
                Arrays.asList(Constants.SIGNUP_SHEET_TEXT_CHANNEL_ID)
        );
    }

    @Override
    public void execute() throws Exception {
        Category category = getEvent()
                .getGuild()
                .getCategoryById(Constants.ARCHIVED_GROUPS_CATEGORY_ID);

        getEvent()
                .getGuild()
                .getTextChannelById(getEvent().getOption("channel").getAsChannel().getId())
                .getManager()
                .setParent(category)
                .sync()
                .queue();
        Util.updateGroupsMessage(getEvent(), Arrays.asList(getEvent().getChannel().getId()));
    }
}
