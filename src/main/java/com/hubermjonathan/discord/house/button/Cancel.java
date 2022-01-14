package com.hubermjonathan.discord.house.button;

import com.hubermjonathan.discord.house.model.GuestButton;

public class Cancel extends GuestButton {
    public Cancel(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        if (!getEvent().getMember().equals(getEvent().getMessage().getMentionedMembers().get(1))) {
            throw new Exception();
        }

        getEvent().deferEdit().queue();
        getEvent().getMessage().delete().queue();
    }
}
