package com.hubermjonathan.discord.mitch.events.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import com.hubermjonathan.discord.mitch.Constants;
import com.hubermjonathan.discord.mitch.events.Util;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.Arrays;

public class CreateEvent extends BotOwnerCommand {
    public CreateEvent() {
        super(
                "event",
                Commands
                        .slash("event", "create an event")
                        .addOption(OptionType.STRING, "title", "the title of the event", true)
                        .addOption(OptionType.STRING, "description", "the description of the event", true)
                        .addOption(OptionType.STRING, "date", "the date of the event", true)
                        .addOption(OptionType.STRING, "time", "the time of the event", true)
                        .addOption(OptionType.STRING, "picture", "the time of the event", false),
                Arrays.asList(Constants.SEATTLE_TEXT_CHANNEL_ID)
        );
    }

    @Override
    public void execute() throws Exception {
        Message message = getEvent()
                .getChannel()
                .asTextChannel()
                .sendMessage((MessageCreateData) Util.buildEventMessage(null, getEvent().getOptions()))
                .complete();

        getEvent()
                .getChannel()
                .asTextChannel()
                .pinMessageById(message.getId())
                .queue();
    }
}
