package com.hubermjonathan.discord.common.models;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class BotOwnerCommand extends Command {
    private final String botOwnerId;

    public BotOwnerCommand(String name, CommandData commandData, String botOwnerId) {
        super(name, commandData);

        this.botOwnerId = botOwnerId;
    }

    @Override
    protected boolean shouldIgnoreEvent() {
        return getEvent().getUser().isBot()
                || !getEvent().getName().equals(getName())
                || !getEvent().getUser().getId().equals(botOwnerId);
    }
}
