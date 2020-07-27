package com.hubermjonathan.mitch;

import com.hubermjonathan.mitch.admin.AdminCommands;
import com.hubermjonathan.mitch.help.HelpCommands;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class Dispatcher extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String content = message.getContentRaw();
        String[] tokens = content.split(" ");

        if (event.getAuthor().isBot()) return;
        if (!channel.getId().equals(Constants.BOT_CHANNEL_ID)) return;

        switch (tokens[0]) {
            case ("admin"):
                AdminCommands adminCommands = new AdminCommands(event);
                adminCommands.dispatch();
                break;
            case ("economy"):
                break;
            case ("help"):
                HelpCommands helpCommands = new HelpCommands(event);
                helpCommands.dispatch();
                break;
            case ("shop"):
                break;
            default:
                message.addReaction(Constants.NO_COMMAND).queue();
        }
    }
}
