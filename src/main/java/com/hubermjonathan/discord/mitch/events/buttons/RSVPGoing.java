package com.hubermjonathan.discord.mitch.events.buttons;

import com.hubermjonathan.discord.common.models.Button;
import com.hubermjonathan.discord.mitch.events.Util;

public class RSVPGoing extends Button {
    public RSVPGoing() {
        super("rsvpGoing");
    }

    @Override
    public void execute() throws Exception {
        getEvent()
                .editMessageEmbeds(
                        Util.addMemberToGoingField(
                                getEvent().getMember().getEffectiveName(),
                                getEvent().getMessage().getEmbeds().get(0)
                        )
                )
                .queue();
    }
}
