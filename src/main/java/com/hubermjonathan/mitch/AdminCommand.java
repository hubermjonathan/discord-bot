package com.hubermjonathan.mitch;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class AdminCommand extends Command {
    public AdminCommand(String command, List<String> aliases) {
        super(command, aliases);
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
        if (!message.getAuthor().getId().equals(message.getGuild().getOwnerId())) {
            message.addReaction(Constants.DENY).queue();
            return;
        }

        setArgs(Arrays.copyOfRange(tokens, 1, tokens.length));
        setEvent(event);

        try {
            executeCommand();
            message.addReaction(Constants.CONFIRM).queue();
        } catch (Exception ignored) {
        }
    }
}
