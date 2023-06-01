package com.hubermjonathan.discord.mitch.management;

import com.hubermjonathan.discord.common.models.Feature;
import com.hubermjonathan.discord.mitch.management.commands.MakeFriend;
import com.hubermjonathan.discord.mitch.management.commands.UploadEmoji;
import com.hubermjonathan.discord.mitch.management.managers.ManageMusicChannel;
import com.hubermjonathan.discord.mitch.management.managers.ManageStrangers;
import com.hubermjonathan.discord.mitch.management.managers.ManageThreads;
import com.hubermjonathan.discord.mitch.management.tasks.KickStrangers;

public class Management extends Feature {
    public Management(boolean enabled) {
        super(enabled);
    }

    @Override
    public void create() {
        addCommand(new MakeFriend());
        addCommand(new UploadEmoji());

        addManager(new ManageMusicChannel());
        addManager(new ManageStrangers());
        addManager(new ManageThreads());
    }

    @Override
    public void createTasks() {
        addTask(new KickStrangers(getGuild(), 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 12));
    }
}
