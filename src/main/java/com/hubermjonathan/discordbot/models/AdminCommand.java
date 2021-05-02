package com.hubermjonathan.discordbot.models;

import com.hubermjonathan.discordbot.Constants;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class AdminCommand extends Command {
    public AdminCommand(String command) {
        super(command);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String[] tokens = message.getContentRaw().split(" ");

        if (user.isBot()) return;
        if (!tokens[0].equals(String.format("<@!%s>", channel.getJDA().getSelfUser().getId()))) return;
        if (tokens.length == 1) return;
        if (!tokens[1].equals(getCommand())) return;
        if (!user.getId().equals(Constants.BOT_OWNER_ID) && !message.getMember().isOwner()) return;

        setArgs(Arrays.copyOfRange(tokens, 2, tokens.length));
        setEvent(event);

        try {
            execute();
            message.addReaction(Constants.CONFIRM).queue();
        } catch (Exception ignored) {
        }
    }
}
