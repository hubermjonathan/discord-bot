package com.hubermjonathan.discord.mitch.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import com.hubermjonathan.discord.mitch.utils.EventUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class EditEvent extends BotOwnerCommand {
    public EditEvent(final String name, final String description) {
        super(
                name,
                Commands.slash(name, description)
                        .addOption(OptionType.STRING, "event", "the id of the event", true)
                        .addOption(OptionType.STRING, "title", "the title of the event", false)
                        .addOption(OptionType.STRING, "description", "the description of the event", false)
                        .addOption(OptionType.STRING, "date", "the date of the event", false)
                        .addOption(OptionType.STRING, "time", "the time of the event", false)
                        .addOption(OptionType.STRING, "picture", "the time of the event", false)
        );
    }

    @Override
    public void execute() throws Exception {
        final Message message = getEvent().getChannel().asTextChannel()
                .retrieveMessageById(getEvent().getOption("event").getAsString()).complete();

        message.editMessage(EventUtils.buildEventMessage(message, getEvent().getOptions())).queue();
    }
}
