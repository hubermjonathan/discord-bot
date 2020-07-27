package com.hubermjonathan.mitch;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandSet {
    private final MessageReceivedEvent event;

    public CommandSet(MessageReceivedEvent event) {
        this.event = event;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }
}
