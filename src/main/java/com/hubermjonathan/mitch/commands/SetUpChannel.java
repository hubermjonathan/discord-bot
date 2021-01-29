package com.hubermjonathan.mitch.commands;

import com.hubermjonathan.mitch.AdminCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
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
        Guild guild = getEvent().getGuild();
        List<String> groups = guild.getRoles().subList(7, guild.getRoles().size() - 1).stream().map(Role::getName).collect(Collectors.toList());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Mitch Bot");
        embedBuilder.setThumbnail(guild.getIconUrl());
        embedBuilder.setDescription(String.format("Mention <@!%s> to use the commands", textChannel.getJDA().getSelfUser().getId()));
        embedBuilder.setFooter("Last updated: " + OffsetDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE), getEvent().getJDA().getSelfUser().getEffectiveAvatarUrl());
        embedBuilder.setColor(0xCFB991);
        embedBuilder.addField("Admin Commands", "```region <newRegion>\nsetup\npriority\nupload <emojiFile>```", false);
        embedBuilder.addField("Global Commands", "```join <group>\nleave <group>```", false);
        embedBuilder.addField("Current Groups", String.format("```%s```", String.join(", ", groups)), false);

        textChannel.deleteMessages(messages).queue();
        textChannel.sendMessage(embedBuilder.build()).queue();
    }
}
