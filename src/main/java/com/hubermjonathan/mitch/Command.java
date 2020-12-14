package com.hubermjonathan.mitch;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class Command extends ListenerAdapter {
    private final String command;
    private final List<String> aliases;
    private String[] args;
    private MessageReceivedEvent event;

    public Command(String command, List<String> aliases) {
        this.command = command;
        this.aliases = aliases;
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
        boolean isCorrect = false;

        if (token.equals(command)) isCorrect = true;
        for (String alias : aliases) {
            if (token.equals(alias)) {
                isCorrect = true;
                break;
            }
        }

        return !isCorrect;
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

        setArgs(Arrays.copyOfRange(tokens, 1, tokens.length));
        setEvent(event);

        try {
            executeCommand();
            message.addReaction(Constants.CONFIRM).queue();
        } catch (Exception ignored) {
        }
    }

    public abstract void executeCommand() throws Exception;
}
