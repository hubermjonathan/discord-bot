package com.hubermjonathan.discord.mitch.buttons;

import com.hubermjonathan.discord.mitch.models.EventButton;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class Maybe extends EventButton {
    public Maybe(String name) {
        super(name);
    }

    private MessageEmbed editEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder(getEvent().getMessage().getEmbeds().get(0));
        List<MessageEmbed.Field> fields = embedBuilder.getFields();

        MessageEmbed.Field goingField = embedBuilder.getFields().remove(0);
        fields.add(0, new MessageEmbed.Field(goingField.getName(), removeFromField(goingField), true));

        MessageEmbed.Field notGoingField = embedBuilder.getFields().remove(1);
        fields.add(1, new MessageEmbed.Field(notGoingField.getName(), removeFromField(notGoingField), true));

        MessageEmbed.Field maybeField = embedBuilder.getFields().remove(2);
        fields.add(2, new MessageEmbed.Field(maybeField.getName(), addToField(maybeField), true));

        return embedBuilder.build();
    }

    @Override
    public void execute() throws Exception {
        getEvent().editMessageEmbeds(editEmbed()).queue();
    }
}
