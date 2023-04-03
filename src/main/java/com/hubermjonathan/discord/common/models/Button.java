package com.hubermjonathan.discord.common.models;

import com.hubermjonathan.discord.common.Constants;
import com.hubermjonathan.discord.common.Logger;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class Button extends ListenerAdapter {
    private final String id;
    private ButtonInteractionEvent event;

    public Button(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ButtonInteractionEvent getEvent() {
        return event;
    }

    protected boolean shouldIgnoreEvent() {
        return event.getUser().isBot() || !event.getComponentId().equals(id);
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        this.event = event;

        if (shouldIgnoreEvent()) {
            return;
        }

        try {
            execute();
            getEvent()
                    .deferEdit()
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
