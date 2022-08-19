package com.hubermjonathan.discord.mitch.models;

public abstract class BotOwnerEventButton extends EventButton {
    public BotOwnerEventButton(final String name) {
        super(name);
    }

    @Override
    protected boolean shouldIgnoreEvent() {
        return getEvent().getUser().isBot()
                || !getEvent().getComponentId().equals(getName())
                || !getEvent().getUser().getId().equals(System.getenv("BOT_OWNER_ID"));
    }
}
