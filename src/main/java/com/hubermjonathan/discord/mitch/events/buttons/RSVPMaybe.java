package com.hubermjonathan.discord.mitch.events.buttons;

import com.hubermjonathan.discord.common.models.Button;
import com.hubermjonathan.discord.mitch.events.EventsUtil;

public class RSVPMaybe extends Button {
    public RSVPMaybe(final String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        getEvent()
                .editMessageEmbeds(
                        EventsUtil.addMemberToMaybeField(
                                getEvent().getMember().getEffectiveName(),
                                getEvent().getMessage().getEmbeds().get(0)
                        )
                )
                .queue();
    }
}
