package com.hubermjonathan.discord.house;


public class Constants {
    public static final String CONFIRM = "U+1F44D";
    public static final String DENY = "U+1F44E";
    public static final String POKE = "U+1F449";
    public static final String POKE_SOUND_FEMALE = "https://www.youtube.com/watch?v=s-KcXdYysTQ";
    public static final String POKE_SOUND_MALE = "https://www.youtube.com/watch?v=mKgoc8axmEU";

    public static final String RESIDENT_ROLE_NAME = "resident";
    public static final String RESIDENT_ROLE_COLOR = "2ecc71";
    public static final String FRIEND_ROLE_NAME = "friend";
    public static final String FRIEND_ROLE_COLOR = "1abc9c";
    public static final String VISITOR_ROLE_NAME = "visitor";
    public static final String VISITOR_ROLE_COLOR = "3498db";

    public static final String MAIN_CATEGORY_NAME = "__________🏠__________";
    public static final String MENTION_TEXT_CHANNEL_NAME = "doorbell";
    public static final String MAIN_TEXT_CHANNEL_NAME = "living-room";

    public static final String ROOMS_CATEGORY_NAME = "__________🚪__________";
    public static final String ROOM_DEFAULT_NAME = "'s room";

    public static String getResidentRoomDefaultName(String residentName) {
        return residentName + ROOM_DEFAULT_NAME;
    }
}
