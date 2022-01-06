package com.hubermjonathan.discord.house.button;

import com.hubermjonathan.discord.house.model.Button;

public class Deny extends Button {
    public Deny(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        getEvent().deferEdit().queue();
        getEvent().getMessage().delete().queue();
    }
}
