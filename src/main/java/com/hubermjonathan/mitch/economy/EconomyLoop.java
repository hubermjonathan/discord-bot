package com.hubermjonathan.mitch.economy;

import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class EconomyLoop extends TimerTask {
    Guild guild;
    Jedis jedis;

    public EconomyLoop(Guild guild) {
        this.guild = guild;
        this.jedis = new Jedis(Constants.REDIS_URL);
    }

    @Override
    public void run() {
        for (VoiceChannel vc : guild.getVoiceChannels()) {
            if (vc.getMembers().size() < 2) continue;

            for (Member m : vc.getMembers()) {
                if (m.getUser().isBot() || m.getRoles().contains(guild.getRoleById(Constants.DEFAULT_ROLE_ID))) continue;

                Map<String, String> oldMapping = jedis.hgetAll(m.getId());
                if (!oldMapping.containsKey("points")) {
                    Map<String, String> defaults = new HashMap<>();
                    defaults.put("points", "0");
                    defaults.put("total_points", "0");
                    defaults.put("last_rmute", String.valueOf(System.currentTimeMillis() / 1000));
                    defaults.put("last_mute", String.valueOf(System.currentTimeMillis() / 1000));
                    defaults.put("last_shield", String.valueOf(System.currentTimeMillis() / 1000));
                    defaults.put("last_muted", String.valueOf(System.currentTimeMillis() / 1000));
                    jedis.hmset(m.getId(), defaults);
                }

                double oldPoints = Double.parseDouble(oldMapping.get("points"));
                double oldTotalPoints = Double.parseDouble(oldMapping.get("total_points"));
                Map<String, String> newMapping = new HashMap<>();
                newMapping.put("points", String.valueOf(oldPoints + 13.75));
                newMapping.put("total_points", String.valueOf(oldTotalPoints + 13.75));
                jedis.hmset(m.getId(), newMapping);
            }
        }
    }
}
