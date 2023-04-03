package com.hubermjonathan.discord.mitch.groups.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import com.hubermjonathan.discord.mitch.Constants;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Arrays;

public class CreateGroup extends BotOwnerCommand {
    public CreateGroup() {
        super(
                "group",
                Commands
                        .slash("group", "create a group")
                        .addOption(OptionType.STRING, "name", "the name of the group to create", true),
                Arrays.asList(Constants.SIGNUP_SHEET_TEXT_CHANNEL_ID)
        );
    }

    @Override
    public void execute() throws Exception {
        Category category = getEvent()
                .getGuild()
                .getCategoryById(Constants.PUBLIC_GROUPS_CATEGORY_ID);

        category
                .createTextChannel(getEvent().getOption("name").getAsString())
                .queue();
    }
}
