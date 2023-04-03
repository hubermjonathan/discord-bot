package com.hubermjonathan.discord.common.models;

import com.hubermjonathan.discord.common.Constants;
import com.hubermjonathan.discord.common.Logger;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class Command extends ListenerAdapter {
    private final String name;
    private final CommandData commandData;
    private final List<String> whitelistedChannels;
    private SlashCommandInteractionEvent event;

    public Command(String name, CommandData commandData, @Nullable List<String> whitelistedChannels) {
        this.name = name;
        this.commandData = commandData;
        this.whitelistedChannels = whitelistedChannels;
    }

    public String getName() {
        return name;
    }

    public CommandData getCommandData() {
        return commandData;
    }

    public SlashCommandInteractionEvent getEvent() {
        return event;
    }

    protected boolean shouldIgnoreEvent() {
        return event.getUser().isBot()
                || !event.getName().equals(name)
                || (System.getenv("DEV") == null && whitelistedChannels != null && !whitelistedChannels.contains(event.getChannel().getId()))
                || (System.getenv("DEV") != null && !event.getChannel().getId().equals(Constants.BOT_TESTING_CHANNEL_ID));
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        this.event = event;

        if (shouldIgnoreEvent()) {
            return;
        }

        try {
            execute();
            getEvent()
                    .reply(Emoji.fromUnicode(Constants.CONFIRM_EMOJI).getName())
                    .setEphemeral(true)
                    .queue();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.log(
                    event.getJDA(),
                    "\u26D4 error",
                    String.format("%s", e.getLocalizedMessage())
            );
            getEvent()
                    .reply(Emoji.fromUnicode(Constants.DENY_EMOJI).getName())
                    .setEphemeral(true)
                    .queue();
        }
    }

    public abstract void execute() throws Exception;
}
