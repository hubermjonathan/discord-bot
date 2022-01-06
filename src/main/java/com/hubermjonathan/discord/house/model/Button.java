package com.hubermjonathan.discord.house.model;

import com.hubermjonathan.discord.house.Constants;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class Button extends ListenerAdapter {
    private final String name;
    private ButtonClickEvent event;

    public Button(String name) {
        this.name = name;
    }

    public ButtonClickEvent getEvent() {
        return event;
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getUser().isBot()
                || !event.getTextChannel().getParent().getName().equals(Constants.ROOM_CATEGORY_NAME)
                || !event.getMember().getEffectiveName().equalsIgnoreCase(event.getTextChannel().getName().substring(0, event.getTextChannel().getName().indexOf('-')))
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
