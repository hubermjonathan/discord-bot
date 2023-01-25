package com.hubermjonathan.discord.common.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;

public class GuildAudioManager {
    public final AudioPlayer audioPlayer;
    private final AudioPlayerSendHandler sendHandler;

    public GuildAudioManager(final AudioPlayerManager manager, final Guild guild) {
        this.audioPlayer = manager.createPlayer();
        this.audioPlayer.addListener(new AudioEventAdapter() {
            @Override
            public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                guild
                        .getAudioManager()
                        .closeAudioConnection();
            }
        });
        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }
}
