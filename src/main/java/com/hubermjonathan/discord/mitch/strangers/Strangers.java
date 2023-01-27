package com.hubermjonathan.discord.mitch.strangers;

import com.hubermjonathan.discord.common.models.Feature;
import com.hubermjonathan.discord.mitch.strangers.tasks.KickStrangers;
import com.hubermjonathan.discord.mitch.strangers.managers.ManageStrangers;

public class Strangers extends Feature {
    public Strangers(boolean enabled) {
        super(enabled);
    }

    @Override
    public void create() {
        addManager(new ManageStrangers());
    }

    @Override
    public void createTasks() {
        addTask(new KickStrangers(getGuild(), 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 12));
    }
}
