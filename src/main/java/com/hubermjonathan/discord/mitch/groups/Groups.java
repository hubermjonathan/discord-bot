package com.hubermjonathan.discord.mitch.groups;

import com.hubermjonathan.discord.common.models.Feature;
import com.hubermjonathan.discord.mitch.groups.commands.ArchiveGroup;
import com.hubermjonathan.discord.mitch.groups.commands.CreateGroup;
import com.hubermjonathan.discord.mitch.groups.managers.ManageGroups;

public class Groups extends Feature {
    public Groups(boolean enabled) {
        super(enabled);
    }
    @Override
    public void create() {
        addCommand(new ArchiveGroup("archive", "archive a group"));
        addCommand(new CreateGroup("group", "create a group"));

        addManager(new ManageGroups());
    }

    @Override
    public void createTasks() {
    }
}
