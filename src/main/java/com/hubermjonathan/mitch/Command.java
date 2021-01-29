package com.hubermjonathan.mitch;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class Command extends ListenerAdapter {
    private final String command;
    private final String alias;
    private String[] args;
    private MessageReceivedEvent event;

    public Command(String command, String alias) {
        this.command = command;
        this.alias = alias;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public void setEvent(MessageReceivedEvent event) {
        this.event = event;
    }

    public boolean isNotCorrectCommand(String token) {
        if (alias == null) {
            return !token.equals(command);
        } else {
            return !token.equals(command) && !token.equals(alias);
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String[] tokens = message.getContentRaw().split(" ");

        if (event.getAuthor().isBot()) return;
        if (!channel.getId().equals(Constants.BOT_CHANNEL_ID)) return;
        if (!tokens[0].equals(String.format("<@!%s>", channel.getJDA().getSelfUser().getId()))) return;
        if (tokens.length == 1) return;
        if (isNotCorrectCommand(tokens[1])) return;

        setArgs(Arrays.copyOfRange(tokens, 2, tokens.length));
        setEvent(event);

        try {
            executeCommand();
            message.addReaction(Constants.CONFIRM).queue();
        } catch (Exception ignored) {
        }
    }

    public abstract void executeCommand() throws Exception;
}
