package com.hubermjonathan.mitch.events;

import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class TrackConnectionTime extends ListenerAdapter {
    private final Jedis jedis;

    public TrackConnectionTime() {
        this.jedis = Constants.JEDIS;
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        jedis.hset(event.getMember().getId(), "sessionStart", String.valueOf(System.currentTimeMillis() / 1000));
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        String memberId = event.getMember().getId();
        Map<String, String> data = jedis.hgetAll(memberId);
        long sessionStart = Long.parseLong(data.get("sessionStart"));
        long sessionLength = (System.currentTimeMillis() / 1000) - sessionStart;

        if (data.get("timeConnected") == null) {
            jedis.hset(memberId, "timeConnected", String.valueOf(sessionLength));
        } else {
            jedis.hincrBy(memberId, "timeConnected", sessionLength);
        }
    }
}
