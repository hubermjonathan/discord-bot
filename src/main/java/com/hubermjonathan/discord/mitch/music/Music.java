package com.hubermjonathan.discord.mitch.music;

import com.hubermjonathan.discord.common.models.Feature;
import com.hubermjonathan.discord.mitch.music.managers.ManageMusicChannel;

public class Music extends Feature {
    public Music(boolean enabled) {
        super(enabled);
    }

    @Override
    public void create() {
        addEvent(new ManageMusicChannel());
    }

    @Override
    public void createTasks() {
    }
}
