package com.hubermjonathan.discord.house.button;

import com.hubermjonathan.discord.house.model.Button;

public class Confirm extends Button {
    public Confirm(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        if (getEvent().getMessage().getMentionedMembers().get(1).getVoiceState().inAudioChannel()) {
            getEvent().getGuild().moveVoiceMember(getEvent().getMessage().getMentionedMembers().get(1), getEvent().getTextChannel().getParentCategory().getVoiceChannels().get(0)).queue();
        }

        getEvent().deferEdit().queue();
        getEvent().getMessage().delete().queue();
    }
}
