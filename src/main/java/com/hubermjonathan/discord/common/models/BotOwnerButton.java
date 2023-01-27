package com.hubermjonathan.discord.common.models;

public abstract class BotOwnerButton extends Button {
    private final String botOwnerId;

    public BotOwnerButton(String id, String botOwnerId) {
        super(id);

        this.botOwnerId = botOwnerId;
    }

    @Override
    protected boolean shouldIgnoreEvent() {
        return getEvent().getUser().isBot()
                || !getEvent().getComponentId().equals(getId())
                || !getEvent().getUser().getId().equals(botOwnerId);
    }
}
