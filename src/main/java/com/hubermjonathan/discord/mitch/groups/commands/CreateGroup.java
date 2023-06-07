package com.hubermjonathan.discord.mitch.groups.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import com.hubermjonathan.discord.common.models.Context;
import com.hubermjonathan.discord.mitch.Constants;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreateGroup extends BotOwnerCommand {
    public CreateGroup(@NotNull Context context) {
        super(
                "group",
                Commands
                        .slash("group", "create a group")
                        .addOption(OptionType.STRING, "name", "the name of the group to create", true),
                context,
                List.of(Constants.SIGNUP_SHEET_TEXT_CHANNEL_ID)
        );
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) throws Exception {
        Category category = event
                .getGuild()
                .getCategoryById(Constants.PUBLIC_GROUPS_CATEGORY_ID);

        category
                .createTextChannel(event.getOption("name").getAsString())
                .queue();
    }
}
