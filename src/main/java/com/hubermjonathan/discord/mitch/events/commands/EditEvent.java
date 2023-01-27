package com.hubermjonathan.discord.mitch.events.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import com.hubermjonathan.discord.mitch.Constants;
import com.hubermjonathan.discord.mitch.events.Util;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

public class EditEvent extends BotOwnerCommand {
    public EditEvent(String name, String description) {
        super(
                name,
                Commands
                        .slash(name, description)
                        .addOption(OptionType.STRING, "event", "the id of the event", true)
                        .addOption(OptionType.STRING, "title", "the title of the event", false)
                        .addOption(OptionType.STRING, "description", "the description of the event", false)
                        .addOption(OptionType.STRING, "date", "the date of the event", false)
                        .addOption(OptionType.STRING, "time", "the time of the event", false)
                        .addOption(OptionType.STRING, "picture", "the time of the event", false)
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED),
                Constants.BOT_OWNER_ID
        );
    }

    @Override
    public void execute() throws Exception {
        Message message = getEvent()
                .getChannel()
                .asTextChannel()
                .retrieveMessageById(getEvent().getOption("event").getAsString())
                .complete();

        message
                .editMessage((MessageEditData) Util.buildEventMessage(message, getEvent().getOptions()))
                .queue();
    }
}
