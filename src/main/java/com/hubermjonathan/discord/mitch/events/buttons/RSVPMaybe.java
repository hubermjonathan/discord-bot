package com.hubermjonathan.discord.mitch.events.buttons;

import com.hubermjonathan.discord.common.models.Button;
import com.hubermjonathan.discord.mitch.events.Util;

public class RSVPMaybe extends Button {
    public RSVPMaybe(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        getEvent()
                .editMessageEmbeds(
                        Util.addMemberToMaybeField(
                                getEvent().getMember().getEffectiveName(),
                                getEvent().getMessage().getEmbeds().get(0)
                        )
                )
                .queue();
    }
}
