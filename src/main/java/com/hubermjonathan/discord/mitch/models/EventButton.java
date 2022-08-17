package com.hubermjonathan.discord.mitch.models;

import com.hubermjonathan.discord.house.Constants;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class EventButton extends ListenerAdapter {
    private final String name;
    private ButtonInteractionEvent event;

    public EventButton(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ButtonInteractionEvent getEvent() {
        return event;
    }

    protected boolean shouldIgnoreEvent() {
        return getEvent().getUser().isBot() || !getEvent().getComponentId().equals(getName());
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        this.event = event;

        if (shouldIgnoreEvent()) {
            return;
        }

        try {
            execute();
        } catch (Exception e) {
            e.printStackTrace();
            getEvent().reply(Emoji.fromUnicode(Constants.DENY).getName()).setEphemeral(true).queue();
        }
    }

    public abstract void execute() throws Exception;
}
