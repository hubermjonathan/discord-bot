package com.hubermjonathan.mitch;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Commands {
    private final MessageReceivedEvent event;

    public Commands(MessageReceivedEvent event) {
        this.event = event;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public void dispatch() {
    }
}
