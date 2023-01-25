package com.hubermjonathan.discord.mitch.events.buttons;

import com.hubermjonathan.discord.common.models.Button;
import com.hubermjonathan.discord.mitch.events.EventsUtil;

public class RSVPNotGoing extends Button {
    public RSVPNotGoing(final String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        getEvent()
                .editMessageEmbeds(
                        EventsUtil.addMemberToNotGoingField(
                                getEvent().getMember().getEffectiveName(),
                                getEvent().getMessage().getEmbeds().get(0)
                        )
                )
                .queue();
    }
}
