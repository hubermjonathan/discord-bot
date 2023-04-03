package com.hubermjonathan.discord.common.models;

public abstract class BotOwnerButton extends Button {
    public BotOwnerButton(String id) {
        super(id);
    }

    @Override
    protected boolean shouldIgnoreEvent() {
        return getEvent().getUser().isBot()
                || !getEvent().getComponentId().equals(getId())
                || !getEvent().getUser().getId().equals(System.getenv("BOT_OWNER_ID"));
    }
}
