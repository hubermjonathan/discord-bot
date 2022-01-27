package com.hubermjonathan.discord.house.player;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildAudioManager> guildAudioManagerMap;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.guildAudioManagerMap = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
    }

    public GuildAudioManager getGuildAudioManager(Guild guild) {
        return this.guildAudioManagerMap.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildAudioManager guildAudioManager = new GuildAudioManager(this.audioPlayerManager, guild);

            guild.getAudioManager().setSendingHandler(guildAudioManager.getSendHandler());

            return guildAudioManager;
        });
    }

    public void loadAndPlay(Guild guild, AudioChannel audioChannel, String trackUrl) {
        final GuildAudioManager guildAudioManager = this.getGuildAudioManager(guild);

        this.audioPlayerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                guild.getAudioManager().openAudioConnection(audioChannel);
                guildAudioManager.audioPlayer.startTrack(track, true);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                //
            }

            @Override
            public void noMatches() {
                //
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                //
            }
        });
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}
