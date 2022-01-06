package com.hubermjonathan.discord.house.button;

import com.hubermjonathan.discord.house.model.Button;
import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;

public class Unlock extends Button {
    public Unlock(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        getEvent().getTextChannel().getParent().getVoiceChannels().get(0).getManager()
                .clearOverridesAdded()
                .putPermissionOverride(getEvent().getGuild().getPublicRole(), EnumSet.of(Permission.VOICE_CONNECT), null)
                .queue();
        getEvent().deferEdit().queue();
    }
}
