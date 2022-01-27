package com.hubermjonathan.discord.house.model;

import com.hubermjonathan.discord.house.Constants;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class GuestButton extends ListenerAdapter {
    private final String name;
    private ButtonInteractionEvent event;

    public GuestButton(String name) {
        this.name = name;
    }

    public ButtonInteractionEvent getEvent() {
        return event;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getUser().isBot()
                || !event.getTextChannel().getParentCategory().getName().equals(Constants.ROOM_CATEGORY_NAME)
                || event.getMember().getEffectiveName().equalsIgnoreCase(event.getTextChannel().getName().substring(0, event.getTextChannel().getName().indexOf('-')))
                || !event.getComponentId().equals(name)) {
            return;
        }

        this.event = event;

        try {
            execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void execute() throws Exception;
}
