package com.hubermjonathan.discord.mitch.events.buttons;

import com.hubermjonathan.discord.common.models.BotOwnerButton;
import com.hubermjonathan.discord.mitch.Constants;

public class DeleteEvent extends BotOwnerButton {
    public DeleteEvent(String name) {
        super(name, Constants.BOT_OWNER_ID);
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
