package com.hubermjonathan.mitch.commands;

import com.hubermjonathan.mitch.Command;
import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ShowLeaderboards extends Command {
    private final Jedis jedis;

    public ShowLeaderboards() {
        super("leaderboards", Arrays.asList("l"));
        this.jedis = Constants.JEDIS;
    }

    private String convertSeconds(Long rawTime) {
        Long days = rawTime / (60 * 60 * 24);
        rawTime = rawTime % (60 * 60 * 24);
        Long hours = rawTime / (60 * 60);
        rawTime = rawTime % (60 * 60);
        Long minutes = rawTime / (60);

        if (hours == 0) {
            return String.format("%dm", minutes);
        } else if (days == 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%dd %dh %dm", days, hours, minutes);
        }
    }

    @Override
    public void executeCommand() {
        Set<String> memberIds = jedis.keys("*");
        List<Long> topTimes = new ArrayList<>();
        List<String> topIds = new ArrayList<>();

        for (String memberId : memberIds) {
            System.out.println(jedis.hgetAll(memberId));
            long timeConnected = Long.parseLong(jedis.hget(memberId, "timeConnected"));

            if (topTimes.size() == 0) {
                topTimes.add(timeConnected);
                topIds.add(memberId);
                continue;
            }

            for (int i = 0; i < topTimes.size(); i++) {
                if (timeConnected > topTimes.get(i)) {
                    topTimes.add(i, timeConnected);
                    topIds.add(i, memberId);
                }

                if (topTimes.size() > 5) {
                    topTimes = topTimes.subList(0, 4);
                    topIds = topIds.subList(0, 4);
                }
            }
        }

        for (int i = 0; i < topTimes.size() - 1; i++) {
            for (int j = 0; j < topTimes.size() - i - 1; j++) {
                if (topTimes.get(j) > topTimes.get(j + 1)) {
                    long tempTime = topTimes.get(j);
                    String tempId = topIds.get(j);
                    topTimes.set(j, topTimes.get(j + 1));
                    topIds.set(j, topIds.get(j + 1));
                    topTimes.set(j + 1, tempTime);
                    topIds.set(j + 1, tempId);
                }
            }
        }

        String leaderboard = "";
        for (int i = 0; i < topTimes.size(); i++) {
            String name = getEvent().getGuild().getMemberById(topIds.get(i)).getEffectiveName();
            leaderboard = leaderboard.concat(String.format("**%d.** %s - %s\n", i + 1, name, convertSeconds(topTimes.get(i))));
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Connection Time Leaderboards");
        embedBuilder.setDescription(leaderboard);
        getEvent().getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
