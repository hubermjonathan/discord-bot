package com.hubermjonathan.discord.mitch.events.buttons;

import com.hubermjonathan.discord.common.models.Button;
import com.hubermjonathan.discord.mitch.events.EventsUtil;

public class RSVPGoing extends Button {
    public RSVPGoing(final String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        getEvent()
                .editMessageEmbeds(
                        EventsUtil.addMemberToGoingField(
                                getEvent().getMember().getEffectiveName(),
                                getEvent().getMessage().getEmbeds().get(0)
                        )
                )
                .queue();
    }
}
