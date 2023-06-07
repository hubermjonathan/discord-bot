package com.hubermjonathan.discord.common.player

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.entities.Guild

class GuildAudioManager(manager: AudioPlayerManager, guild: Guild) {
    val audioPlayer: AudioPlayer = manager.createPlayer()
    val sendHandler = AudioPlayerSendHandler(audioPlayer)

    init {
        audioPlayer.addListener(object : AudioEventAdapter() {
            override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
                guild.audioManager.closeAudioConnection()
            }
        })
    }
}
