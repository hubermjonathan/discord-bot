package com.hubermjonathan.mitch.help;

import com.hubermjonathan.mitch.Commands;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommands extends Commands {
    public HelpCommands(MessageReceivedEvent event) {
        super(event);
    }

    @Override
    public void dispatch() {
        sendHelpMessage();
    }

    private void sendHelpMessage() {
        MessageChannel channel = getEvent().getChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("commands");
        embedBuilder.setDescription(
                "**ex: command (alias) - ** `command_name [required_argument] {optional_argument}`\n"
                .concat("check your balance - `balance`\n")
                .concat("send someone money - `send [person] [amount]`\n")
                .concat("show the shop - `shop`\n")
        );
        channel.sendMessage(embedBuilder.build()).queue();
    }
}
