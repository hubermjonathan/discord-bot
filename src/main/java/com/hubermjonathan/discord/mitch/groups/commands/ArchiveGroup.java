package com.hubermjonathan.discord.mitch.groups.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import com.hubermjonathan.discord.mitch.Constants;
import com.hubermjonathan.discord.mitch.groups.Util;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Arrays;

public class ArchiveGroup extends BotOwnerCommand {
    public ArchiveGroup(String name, String description) {
        super(
                name,
                Commands
                        .slash(name, description)
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED),
                Constants.BOT_OWNER_ID
        );
    }

    @Override
    public void execute() throws Exception {
        Category category = getEvent()
                .getGuild()
                .getCategoriesByName(Constants.ARCHIVED_GROUPS_CATEGORY_NAME, true)
                .get(0);

        getEvent()
                .getChannel()
                .asTextChannel()
                .getManager()
                .setParent(category)
                .sync()
                .queue();
        Util.updateGroupsMessage(getEvent(), Arrays.asList(getEvent().getOption("channel").getAsString()));
    }
}
