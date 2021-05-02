package com.hubermjonathan.discordbot.buttons;

import com.hubermjonathan.discordbot.models.Button;
import net.dv8tion.jda.api.entities.Category;

public class Deny extends Button {
    public Deny(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        Category category = getEvent().getChannel().getParent();
        category.getTextChannels().get(0).retrieveMessageById(getEvent().getMessageId()).complete().delete().queue();
    }
}
