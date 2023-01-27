package com.hubermjonathan.discord.mitch.draft;

import com.hubermjonathan.discord.common.models.Feature;
import com.hubermjonathan.discord.mitch.draft.commands.StartDraft;

public class Draft extends Feature {
    public Draft(boolean enabled) {
        super(enabled);
    }

    @Override
    public void create() {
        addCommand(new StartDraft("draft", "draft people to hang out"));
    }

    @Override
    public void createTasks() {
    }
}
