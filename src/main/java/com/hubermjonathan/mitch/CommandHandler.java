package com.hubermjonathan.mitch;


import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class CommandHandler extends ListenerAdapter {
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String content = message.getContentRaw();
        String[] tokens = content.split(" ");

        if (event.getAuthor().isBot()) return;
        if (!channel.getId().equals(System.getenv("BOT_CHANNEL_ID"))) return;

        switch (tokens[0]) {
            case ("admin"):
                Admin admin = new Admin(event);
                admin.dispatch();
                break;
            case ("economy"):
                break;
            case ("help"):
                Help help = new Help(event);
                help.dispatch();
                break;
            case ("shop"):
                break;
            default:
                System.out.println("HANDLE NO COMMAND");
        }
    }
}
