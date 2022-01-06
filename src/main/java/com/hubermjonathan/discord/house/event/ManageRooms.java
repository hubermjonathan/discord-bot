package com.hubermjonathan.discord.house.event;

import com.hubermjonathan.discord.house.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class ManageRooms extends ListenerAdapter {


    private void createRoom(@NotNull GenericGuildMemberEvent event) {
        Category roomCategory = event.getGuild().createCategory(Constants.ROOM_CATEGORY_NAME).complete();
        TextChannel doorChannel = roomCategory.createTextChannel(Constants.getResidentDoorName(event.getMember().getEffectiveName()))
                .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.MESSAGE_WRITE))
                .complete();
        roomCategory.createVoiceChannel(Constants.getResidentRoomDefaultName(event.getMember().getEffectiveName()))
                .addRolePermissionOverride(event.getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.VOICE_CONNECT))
                .addMemberPermissionOverride(event.getMember().getIdLong(), EnumSet.of(Permission.VOICE_CONNECT), null)
                .complete();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder
                .setTitle(Constants.getResidentRoomDefaultName(event.getMember().getEffectiveName()))
                .setDescription(Constants.ROOM_DOOR_DEFAULT_TEXT);
        Message message = doorChannel
                .sendMessageEmbeds(embedBuilder.build())
                .setActionRows(
                        ActionRow.of(Constants.GUEST_BUTTONS),
                        ActionRow.of(Constants.BUTTONS)
                )
                .complete();
        message.pin().queue();
    }

    private void deleteRoom(@NotNull GenericGuildMemberEvent event) {
        Category roomCategory = event.getGuild().getTextChannelsByName(Constants.getResidentDoorName(event.getMember().getEffectiveName()), true).get(0).getParent();
        roomCategory.getTextChannels().get(0).delete().queue();
        roomCategory.getVoiceChannels().get(0).delete().queue();
        roomCategory.delete().queue();
    }

    private void deleteRoom(@NotNull GuildMemberRemoveEvent event) {
        Category roomCategory = event.getGuild().getTextChannelsByName(Constants.getResidentDoorName(event.getMember().getEffectiveName()), true).get(0).getParent();
        roomCategory.getTextChannels().get(0).delete().queue();
        roomCategory.getVoiceChannels().get(0).delete().queue();
        roomCategory.delete().queue();
    }

    private void repairRoom(@NotNull Event event) {
        for (Category category : event.getJDA().getCategoriesByName(Constants.ROOM_CATEGORY_NAME, true)) {
            Message doorMessage = category.getTextChannels().get(0).retrievePinnedMessages().complete().get(0);

            if (!doorMessage.getActionRows().get(0).getButtons().equals(Constants.GUEST_BUTTONS)
                    || !doorMessage.getActionRows().get(1).getButtons().equals(Constants.BUTTONS)) {
                doorMessage.editMessageComponents(
                        ActionRow.of(Constants.GUEST_BUTTONS),
                        ActionRow.of(Constants.BUTTONS)
                ).queue();
            }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        repairRoom(event);
    }

    @Override
    public void onReconnected(@NotNull ReconnectedEvent event) {
        repairRoom(event);
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        if (event.getUser().isBot()) {
            return;
        }

        Role visitorRole = event.getGuild().getRolesByName(Constants.VISITOR_ROLE_NAME, true).get(0);
        event.getGuild().addRoleToMember(event.getMember(), visitorRole).queue();
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        if (event.getUser().isBot() || !event.getMember().getRoles().get(0).getName().equals(Constants.RESIDENT_ROLE_NAME)) {
            return;
        }

        deleteRoom(event);
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
            if (!role.getName().equals(event.getRoles().get(0).getName())) {
                event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
            }
        }
    }

    @Override
    public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
        if (event.getUser().isBot()) {
            return;
        }


        if (event.getRoles().get(0).getName().equals(Constants.RESIDENT_ROLE_NAME)) {
            deleteRoom(event);
        }

        Role visitorRole = event.getGuild().getRolesByName(Constants.VISITOR_ROLE_NAME, true).get(0);
        if (event.getMember().getRoles().size() == 0) {
            event.getGuild().addRoleToMember(event.getMember(), visitorRole).queue();
        }
    }

    @Override
    public void onGuildMemberUpdateNickname(@NotNull GuildMemberUpdateNicknameEvent event) {
        if (event.getUser().isBot() || !event.getMember().getRoles().get(0).getName().equals(Constants.RESIDENT_ROLE_NAME)) {
            return;
        }

        String oldName = event.getOldNickname() == null ? event.getUser().getName() : event.getOldNickname();
        String newName = event.getNewNickname() == null ? event.getUser().getName() : event.getNewNickname();
        TextChannel doorChannel = event.getGuild().getTextChannelsByName(Constants.getResidentDoorName(oldName), true).get(0);
        doorChannel.getManager().setName(Constants.getResidentDoorName(newName)).queue();
    }
}
