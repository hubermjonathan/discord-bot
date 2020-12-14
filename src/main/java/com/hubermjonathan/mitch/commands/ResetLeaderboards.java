package com.hubermjonathan.mitch.commands;

import com.hubermjonathan.mitch.AdminCommand;
import com.hubermjonathan.mitch.Constants;
import redis.clients.jedis.Jedis;

import java.util.Arrays;

public class ResetLeaderboards extends AdminCommand {
    Jedis jedis;

    public ResetLeaderboards() {
        super("reset", Arrays.asList());
        this.jedis = Constants.JEDIS;
    }

    @Override
    public void executeCommand() {
        jedis.flushDB();
    }
}
