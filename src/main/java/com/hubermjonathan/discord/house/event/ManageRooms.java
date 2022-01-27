package com.hubermjonathan.discord.house.event;

import com.hubermjonathan.discord.house.Constants;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class ManageRooms extends ListenerAdapter {
    private void createRoom(@NotNull GenericGuildMemberEvent event) {
        Category roomsCategory = event.getGuild().getCategoriesByName(Constants.ROOMS_CATEGORY_NAME, true).get(0);
        roomsCategory.createVoiceChannel(Constants.getResidentRoomDefaultName(event.getMember().getEffectiveName()))
                .complete();
    }

    private void deleteRoom(@NotNull GenericGuildMemberEvent event) {
        event.getGuild().getVoiceChannelsByName(Constants.getResidentRoomDefaultName(event.getMember().getEffectiveName()), true).get(0).delete().queue();
    }

    private void deleteRoom(@NotNull GuildMemberRemoveEvent event) {
        event.getGuild().getVoiceChannelsByName(Constants.getResidentRoomDefaultName(event.getMember().getEffectiveName()), true).get(0).delete().queue();
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
}
