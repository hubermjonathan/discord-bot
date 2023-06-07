package com.hubermjonathan.discord.common.player

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel

object PlayerManager {
    private val guildAudioManagerMap = mutableMapOf<Guild, GuildAudioManager>()
    private val audioPlayerManager = DefaultAudioPlayerManager()

    init {
        AudioSourceManagers.registerLocalSource(audioPlayerManager)
        AudioSourceManagers.registerRemoteSources(audioPlayerManager)
    }

    private fun getGuildAudioManager(guild: Guild): GuildAudioManager {
        return guildAudioManagerMap.computeIfAbsent(guild) {
            GuildAudioManager(audioPlayerManager, it)
        }.also {
            guild.audioManager.sendingHandler = it.sendHandler
        }
    }

    fun loadAndPlay(guild: Guild, audioChannel: AudioChannel, trackUrl: String) {
        val guildAudioManager = getGuildAudioManager(guild)

        audioPlayerManager.loadItem(
            trackUrl,
            object : AudioLoadResultHandler {
                override fun trackLoaded(track: AudioTrack) {
                    guild.audioManager.openAudioConnection(audioChannel)
                    guildAudioManager.audioPlayer.startTrack(track, true)
                }

                override fun playlistLoaded(playlist: AudioPlaylist) {}

                override fun noMatches() {}

                override fun loadFailed(exception: FriendlyException) {}
            },
        )
    }
}
