package com.hubermjonathan.discordbot.buttons;

import com.hubermjonathan.discordbot.models.Button;
import net.dv8tion.jda.api.entities.Category;

public class Cancel extends Button {
    public Cancel(String name, boolean isOwnerControlled) {
        super(name, isOwnerControlled);
    }

    @Override
    public void execute() throws Exception {
        Category category = getEvent().getChannel().getParent();
        category.getTextChannels().get(0).retrieveMessageById(getEvent().getMessageId()).complete().delete().queue();
    }
}
