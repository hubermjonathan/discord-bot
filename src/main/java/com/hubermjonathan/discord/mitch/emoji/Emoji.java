package com.hubermjonathan.discord.mitch.emoji;

import com.hubermjonathan.discord.common.models.Feature;
import com.hubermjonathan.discord.mitch.emoji.commands.UploadEmoji;

public class Emoji extends Feature {
    public Emoji(boolean enabled) {
        super(enabled);
    }

    @Override
    public void create() {
        addCommand(new UploadEmoji("emoji", "upload an emoji"));
    }

    @Override
    public void createTasks() {
    }
}
