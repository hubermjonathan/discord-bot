package com.hubermjonathan.discord.common.models;

import com.hubermjonathan.discord.common.Constants;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;


public abstract class BotOwnerCommand extends ListenerAdapter {
    private final String name;
    private final CommandData commandData;
    private SlashCommandInteractionEvent event;

    public BotOwnerCommand(final String name, final CommandData commandData) {
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
    public void onSlashCommandInteraction(@NotNull final SlashCommandInteractionEvent event) {
        if (!event.getName().equals(name)
                || !event.getUser().getId().equals(System.getenv("BOT_OWNER_ID"))) {
            return;
        }

        this.event = event;

        try {
            execute();
            getEvent()
                    .reply(Emoji.fromUnicode(Constants.CONFIRM_EMOJI).getName())
                    .setEphemeral(true)
                    .queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void execute() throws Exception;
}
