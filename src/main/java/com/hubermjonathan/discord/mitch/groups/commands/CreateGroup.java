package com.hubermjonathan.discord.mitch.groups.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import com.hubermjonathan.discord.mitch.Constants;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class CreateGroup extends BotOwnerCommand {
    public CreateGroup(String name, String description) {
        super(
                name,
                Commands
                        .slash(name, description)
                        .addOption(OptionType.STRING, "name", "the name of the group to create", true)
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED),
                Constants.BOT_OWNER_ID
        );
    }

    @Override
    public void execute() throws Exception {
        Category category = getEvent()
                .getGuild()
                .getCategoriesByName(Constants.PUBLIC_GROUPS_CATEGORY_NAME, true)
                .get(0);
        TextChannel textChannel = category
                .createTextChannel(getEvent().getOption("name").getAsString())
                .complete();
    }
}
