package com.hubermjonathan.discord.mitch.events.buttons;

import com.hubermjonathan.discord.common.models.Button;
import com.hubermjonathan.discord.mitch.events.Util;

public class RSVPNotGoing extends Button {
    public RSVPNotGoing() {
        super("rsvpNotGoing");
    }

    @Override
    public void execute() throws Exception {
        getEvent()
                .editMessageEmbeds(
                        Util.addMemberToNotGoingField(
                                getEvent().getMember().getEffectiveName(),
                                getEvent().getMessage().getEmbeds().get(0)
                        )
                )
                .queue();
    }
}
