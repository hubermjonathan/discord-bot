package com.hubermjonathan.discord.mitch.management.managers;

import com.hubermjonathan.discord.common.models.Manager;
import com.hubermjonathan.discord.mitch.Constants;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public class ManageMusicChannel extends Manager {
    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        if (event.getMember().getUser().isBot() || event.getMember().isOwner()) {
            return;
        }

        TextChannel musicChannel = event
                .getGuild()
                .getTextChannelById(Constants.MUSIC_TEXT_CHANNEL_ID);

        if (event.getChannelJoined() != null && event.getChannelLeft() == null) {
            musicChannel
                    .getManager()
                    .putMemberPermissionOverride(event.getMember().getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .queue();
        } else if (event.getChannelJoined() == null && event.getChannelLeft() != null) {
            List<PermissionOverride> memberPermissionOverrides = musicChannel.getMemberPermissionOverrides();

            for (PermissionOverride memberPermissionOverride : memberPermissionOverrides) {
                if (memberPermissionOverride.getMember().equals(event.getMember())) {
                    musicChannel
                            .getManager()
                            .removePermissionOverride(memberPermissionOverride.getPermissionHolder())
                            .queue();

                    return;
                }
            }
        }
    }
}
