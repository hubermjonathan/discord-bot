package com.hubermjonathan.discord.mitch.buttons;

import com.hubermjonathan.discord.mitch.models.EventButton;
import com.hubermjonathan.discord.mitch.utils.EventUtils;

public class Maybe extends EventButton {
    public Maybe(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        getEvent().editMessageEmbeds(EventUtils.addMemberToMaybeField(
                getEvent().getMember().getEffectiveName(), getEvent().getMessage().getEmbeds().get(0))).queue();
    }
}
