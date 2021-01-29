package com.hubermjonathan.mitch.commands;

import com.hubermjonathan.mitch.AdminCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SetUpChannel extends AdminCommand {
    public SetUpChannel() {
        super("setup", null);
    }

    @Override
    public void executeCommand() {
        TextChannel textChannel = getEvent().getTextChannel();
        Collection<Message> messages = textChannel.getHistoryFromBeginning(100).complete().getRetrievedHistory();
        List<String> groups = getEvent().getGuild().getRoles().subList(7, getEvent().getGuild().getRoles().size() - 1).stream().map(Role::getName).collect(Collectors.toList());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Mitch");
        embedBuilder.setDescription(String.format("Mention <@!%s> to use the commands", textChannel.getJDA().getSelfUser().getId()));
        embedBuilder.setFooter("Last updated: " + OffsetDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE), getEvent().getJDA().getSelfUser().getEffectiveAvatarUrl());
        embedBuilder.setColor(0xCFB991);
        embedBuilder.addField("Current Groups", String.join(", ", groups), false);
        embedBuilder.addField("Commands", "join <group>\nleave <group>", false);

        textChannel.deleteMessages(messages).queue();
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
}
