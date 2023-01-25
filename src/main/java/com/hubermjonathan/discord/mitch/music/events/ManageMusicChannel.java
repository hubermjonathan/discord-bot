package com.hubermjonathan.discord.mitch.music.events;

import com.hubermjonathan.discord.mitch.MitchConstants;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public class ManageMusicChannel extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(@NotNull final GuildVoiceUpdateEvent event) {
        if (event.getMember().getUser().isBot() || event.getMember().isOwner()) {
            return;
        }

        final TextChannel musicChannel = event
                .getGuild()
                .getTextChannelById(MitchConstants.MUSIC_TEXT_CHANNEL_NAME);

        if (event.getChannelJoined() != null && event.getChannelLeft() == null) {
            musicChannel
                    .getManager()
                    .putMemberPermissionOverride(event.getMember().getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .queue();
        } else if (event.getChannelJoined() == null && event.getChannelLeft() != null) {
            final List<PermissionOverride> memberPermissionOverrides = musicChannel.getMemberPermissionOverrides();

            for (final PermissionOverride memberPermissionOverride : memberPermissionOverrides) {
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
