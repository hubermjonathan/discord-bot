package com.hubermjonathan.discord.mitch.buttons;

import com.hubermjonathan.discord.mitch.models.BotOwnerEventButton;

public class Delete extends BotOwnerEventButton {
    public Delete(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        getEvent().deferEdit().queue();
        getEvent().getMessage().delete().queue();
    }
}
