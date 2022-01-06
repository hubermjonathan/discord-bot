package com.hubermjonathan.discord.house.model;

import com.hubermjonathan.discord.house.Constants;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class BotOwnerCommand extends ListenerAdapter {
    private final String name;
    private CommandData commandData;
    private SlashCommandEvent event;

    public BotOwnerCommand(String name, CommandData commandData) {
        this.name = name;
        this.commandData = commandData;
    }

    public CommandData getCommandData() {
        return commandData;
    }

    public SlashCommandEvent getEvent() {
        return event;
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (!event.getName().equals(name)
                || !event.getUser().getId().equals(System.getenv("BOT_OWNER_ID"))) {
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
