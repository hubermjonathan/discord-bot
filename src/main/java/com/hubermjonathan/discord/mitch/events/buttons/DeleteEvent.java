package com.hubermjonathan.discord.mitch.events.buttons;

import com.hubermjonathan.discord.common.models.BotOwnerButton;

public class DeleteEvent extends BotOwnerButton {
    public DeleteEvent() {
        super("deleteEvent");
    }

    @Override
    public void execute() throws Exception {
        getEvent()
                .deferEdit()
                .queue();
        getEvent()
                .getMessage()
                .delete()
                .queue();
    }
}
