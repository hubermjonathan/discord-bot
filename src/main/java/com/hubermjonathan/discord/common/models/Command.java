package com.hubermjonathan.discord.common.models;

import com.hubermjonathan.discord.common.Constants;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;


public abstract class Command extends ListenerAdapter {
    private final String name;
    private final CommandData commandData;
    private SlashCommandInteractionEvent event;

    public Command(final String name, final CommandData commandData) {
        this.name = name;
        this.commandData = commandData;
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
        return event.getUser().isBot() || !event.getName().equals(name);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
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
            getEvent()
                    .reply(Emoji.fromUnicode(Constants.DENY_EMOJI).getName())
                    .setEphemeral(true)
                    .queue();
        }
    }

    public abstract void execute() throws Exception;
}
