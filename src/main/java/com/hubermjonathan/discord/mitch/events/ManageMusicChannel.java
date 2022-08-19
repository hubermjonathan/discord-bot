package com.hubermjonathan.discord.mitch.events;

import com.hubermjonathan.discord.mitch.Constants;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class ManageMusicChannel extends ListenerAdapter {
    @Override
    public void onGuildVoiceJoin(@NotNull final GuildVoiceJoinEvent event) {
        if (event.getMember().getUser().isBot() || event.getMember().isOwner()) {
            return;
        }

        event.getGuild().getTextChannelById(Constants.MUSIC_TEXT_CHANNEL_ID).getManager()
                .putMemberPermissionOverride(event.getMember().getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                .queue();
    }

    @Override
    public void onGuildVoiceLeave(@NotNull final GuildVoiceLeaveEvent event) {
        if (event.getMember().getUser().isBot() || event.getMember().isOwner()) {
            return;
        }

        event.getGuild().getTextChannelById(Constants.MUSIC_TEXT_CHANNEL_ID).getManager()
                .putMemberPermissionOverride(event.getMember().getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .queue();
    }
}
