package com.hubermjonathan.discordbot.buttons;

import com.hubermjonathan.discordbot.models.Button;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Message;

public class Confirm extends Button {
    public Confirm(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        Category category = getEvent().getChannel().getParent();
        Message message = category.getTextChannels().get(0).retrieveMessageById(getEvent().getMessageId()).complete();

        getEvent().getGuild().moveVoiceMember(message.getMentionedMembers().get(1), category.getVoiceChannels().get(0)).queue();
        message.delete().queue();
    }
}
