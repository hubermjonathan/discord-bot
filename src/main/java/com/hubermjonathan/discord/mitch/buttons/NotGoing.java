package com.hubermjonathan.discord.mitch.buttons;

import com.hubermjonathan.discord.mitch.models.EventButton;
import com.hubermjonathan.discord.mitch.utils.EventUtils;

public class NotGoing extends EventButton {
    public NotGoing(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        getEvent().editMessageEmbeds(EventUtils.addMemberToNotGoingField(
                getEvent().getMember().getEffectiveName(), getEvent().getMessage().getEmbeds().get(0))).queue();
    }
}
