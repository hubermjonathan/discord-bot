package com.hubermjonathan.discordbot.events;

import com.hubermjonathan.discordbot.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class ManageHouse extends ListenerAdapter {
    private void createRoom(@NotNull GenericGuildMemberEvent event) {
        Category category = event.getGuild().createCategory(Constants.ROOMS_CATEGORY_NAME).complete();
        TextChannel textChannel = category.createTextChannel(event.getMember().getEffectiveName() + "s-door")
                .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.MESSAGE_WRITE))
                .complete();
        category.createVoiceChannel(event.getMember().getEffectiveName() + "'s room")
                .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VOICE_CONNECT))
                .addMemberPermissionOverride(event.getMember().getIdLong(), EnumSet.of(Permission.VOICE_CONNECT), null)
                .complete();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setTitle(event.getMember().getEffectiveName() + "'s room")
                .setDescription("``` ```");
        Message message = textChannel.sendMessage(embedBuilder.build()).complete();
        message.pin().queue();
        message.addReaction(Constants.KNOCK).queue();
        message.addReaction(Constants.LOCK).queue();
        message.addReaction(Constants.UNLOCK).queue();
        message.addReaction(Constants.KICK).queue();
    }

    private void deleteRoom(@NotNull GenericGuildMemberEvent event) {
        Category category = null;
        for (TextChannel textChannel : event.getGuild().getTextChannels()) {
            if (textChannel.getName().equals(event.getMember().getEffectiveName() + "s-door")) {
                category = textChannel.getParent();
                break;
            }
        }

        category.getTextChannels().get(0).delete().queue();
        category.getVoiceChannels().get(0).delete().queue();
        category.delete().queue();
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (event.getUser().isBot()) {
            return;
        }

        Role visitorRole = null;
        for (Role role : event.getGuild().getRoles()) {
            if (role.getName().equals(Constants.VISITOR_ROLE_NAME)) {
                visitorRole = role;
                break;
            }
        }

        event.getGuild().addRoleToMember(event.getMember(), visitorRole).queue();
    }

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        if (event.getUser().isBot()) {
            return;
        }

        if (event.getRoles().get(0).getName().equals(Constants.RESIDENT_ROLE_NAME)) {
            createRoom(event);
        }

        for (Role role : event.getMember().getRoles()) {
            if (role.getName().equals(event.getRoles().get(0).getName())) {
                continue;
            }

            if (role.getName().equals(Constants.RESIDENT_ROLE_NAME)) {
                deleteRoom(event);
            }

            event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
        }
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        if (event.getUser().isBot() || event.getMember().getRoles().size() != 0) {
            return;
        }

        Role visitorRole = null;
        for (Role role : event.getGuild().getRoles()) {
            if (role.getName().equals(Constants.VISITOR_ROLE_NAME)) {
                visitorRole = role;
                break;
            }
        }

        if (event.getRoles().get(0).getName().equals(Constants.RESIDENT_ROLE_NAME)) {
            deleteRoom(event);
        }

        event.getGuild().addRoleToMember(event.getMember(), visitorRole).queue();
    }

    @Override
    public void onGuildMemberUpdateNickname(@NotNull GuildMemberUpdateNicknameEvent event) {
        if (event.getUser().isBot() || !event.getMember().getRoles().get(0).getName().equals(Constants.RESIDENT_ROLE_NAME)) {
            return;
        }

        String oldName = event.getOldNickname() == null ? event.getUser().getName() : event.getOldNickname();
        String newName = event.getNewNickname() == null ? event.getUser().getName() : event.getNewNickname();

        for (TextChannel textChannel : event.getGuild().getTextChannels()) {
            if (textChannel.getName().equals(oldName + "s-door")) {
                textChannel.getManager().setName(newName + "s-door").queue();
                return;
            }
        }
    }
}
