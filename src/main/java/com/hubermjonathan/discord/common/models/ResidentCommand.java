package com.hubermjonathan.discord.common.models;

import com.hubermjonathan.discord.house.Constants;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public abstract class ResidentCommand extends ListenerAdapter {
    private final String name;
    private CommandData commandData;
    private SlashCommandInteractionEvent event;

    public ResidentCommand(String name, CommandData commandData) {
        this.name = name;
        this.commandData = commandData;
    }

    public CommandData getCommandData() {
        return commandData;
    }

    public SlashCommandInteractionEvent getEvent() {
        return event;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals(name)
                || !event.getMember().getRoles().get(0).getName().equals(Constants.RESIDENT_ROLE_NAME)) {
            return;
        }

        this.event = event;

        try {
            execute();
            getEvent().reply(Emoji.fromUnicode(Constants.CONFIRM).getName()).setEphemeral(true).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void execute() throws Exception;
}
