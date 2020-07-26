package com.hubermjonathan.mitch;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Feature {
    private final MessageReceivedEvent event;

    public Feature(MessageReceivedEvent event) {
        this.event = event;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }
}
