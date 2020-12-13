package com.hubermjonathan.mitch;

import redis.clients.jedis.Jedis;

public class Constants {
    public static final String BOT_CHANNEL_ID = "787488862619697182";
    public static final String DEFAULT_ROLE_ID = "701162007981981771";

    public static final String REDIS_URL = System.getenv("REDIS_URL");
    public static final Jedis JEDIS = new Jedis(REDIS_URL);

    public static final String CONFIRM = "\uD83D\uDC4D";
    public static final String DENY = "\uD83D\uDC4E";
    public static final String NOT_IN_VC = "\uD83D\uDD07";
}
