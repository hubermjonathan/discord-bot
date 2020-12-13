package com.hubermjonathan.mitch.commands;

import com.hubermjonathan.mitch.Command;
import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.entities.Message;
import redis.clients.jedis.Jedis;

import java.util.Arrays;

public class ResetLeaderboards extends Command {
    Jedis jedis;

    public ResetLeaderboards() {
        super("reset", Arrays.asList());
        this.jedis = Constants.JEDIS;
    }

    @Override
    public void executeCommand() {
        Message message = getEvent().getMessage();

        if (!message.getAuthor().getId().equals(message.getGuild().getOwnerId())) {
            message.addReaction(Constants.DENY).queue();
            return;
        }

        jedis.flushDB();

        message.addReaction(Constants.CONFIRM).queue();
    }
}
