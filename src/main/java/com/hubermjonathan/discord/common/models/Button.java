package com.hubermjonathan.discord.common.models;

import com.hubermjonathan.discord.common.Constants;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class Button extends ListenerAdapter {
    private final String id;
    private ButtonInteractionEvent event;

    public Button(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ButtonInteractionEvent getEvent() {
        return event;
    }

    protected boolean shouldIgnoreEvent() {
        return getEvent().getUser().isBot() || !getEvent().getComponentId().equals(getId());
    }

    @Override
    public void onButtonInteraction(@NotNull final ButtonInteractionEvent event) {
        this.event = event;

        if (shouldIgnoreEvent()) {
            return;
        }

        try {
            execute();
            getEvent().reply(Emoji.fromUnicode(Constants.CONFIRM_EMOJI).getName()).setEphemeral(true).queue();
        } catch (Exception e) {
            e.printStackTrace();
            getEvent().reply(Emoji.fromUnicode(Constants.DENY_EMOJI).getName()).setEphemeral(true).queue();
        }
    }

    public abstract void execute() throws Exception;
}
