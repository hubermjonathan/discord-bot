package com.hubermjonathan.discord.house.button;

import com.hubermjonathan.discord.house.model.Button;
import net.dv8tion.jda.api.Permission;

import java.util.EnumSet;

public class Lock extends Button {
    public Lock(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        getEvent().getTextChannel().getParent().getVoiceChannels().get(0).getManager()
                .clearOverridesAdded()
                .putPermissionOverride(getEvent().getGuild().getPublicRole(), null, EnumSet.of(Permission.VOICE_CONNECT))
                .queue();
        getEvent().deferEdit().queue();
    }
}
