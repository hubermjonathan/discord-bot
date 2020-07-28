package com.hubermjonathan.mitch.shop;

import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import redis.clients.jedis.Jedis;

import javax.annotation.Nonnull;

public class ShopEvents extends ListenerAdapter {
    Jedis jedis;

    public ShopEvents() {
        this.jedis = new Jedis(Constants.REDIS_URL);
    }

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        Member member = event.getMember();
        if (!member.getUser().isBot() && member.getVoiceState().isGuildMuted()) {
            long lastMuted = Long.parseLong(jedis.hget(member.getId(), "last_muted"));
            if ((System.currentTimeMillis() / 1000) - lastMuted > 60) member.mute(false).queue();
        }
    }
}
