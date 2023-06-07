package com.hubermjonathan.discord.mitch.groups;

import com.hubermjonathan.discord.common.models.*;
import com.hubermjonathan.discord.mitch.groups.commands.ArchiveGroup;
import com.hubermjonathan.discord.mitch.groups.commands.CreateGroup;
import com.hubermjonathan.discord.mitch.groups.managers.ManageGroups;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Groups extends Feature {
    public Groups() {
        super("groups", "\uD83D\uDC65");
    }

    @NotNull
    @Override
    public List<Button> getButtons() {
        return List.of();
    }

    @NotNull
    @Override
    public List<Command> getCommands() {
        return List.of(new ArchiveGroup(this.getContext()), new CreateGroup(this.getContext()));
    }

    @NotNull
    @Override
    public List<Manager> getManagers() {
        return List.of(new ManageGroups(this.getContext()));
    }

    @NotNull
    @Override
    public List<Task> getTasks() {
        return List.of();
    }
}
