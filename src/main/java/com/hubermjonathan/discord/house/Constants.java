package com.hubermjonathan.discord.house;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String CONFIRM = "U+1F44D";
    public static final String DENY = "U+1F44E";
    public static final String CANCEL = "U+274C";
    public static final String KNOCK = "U+270A";
    public static final String POKE = "U+1F449";
    public static final String POKE_SOUND_FEMALE = "https://www.youtube.com/watch?v=s-KcXdYysTQ";
    public static final String POKE_SOUND_MALE = "https://www.youtube.com/watch?v=mKgoc8axmEU";
    public static final String LOCK = "U+1F512";
    public static final String UNLOCK = "U+1F511";
    public static final String KICK = "U+270C";

    public static final List<Button> GUEST_BUTTONS = Arrays.asList(
            Button.secondary(KNOCK, Emoji.fromUnicode(KNOCK)),
            Button.secondary(POKE, Emoji.fromUnicode(POKE))
    );
    public static final List<Button> BUTTONS = Arrays.asList(
            Button.secondary(LOCK, Emoji.fromUnicode(LOCK)),
            Button.secondary(UNLOCK, Emoji.fromUnicode(UNLOCK)),
            Button.secondary(KICK, Emoji.fromUnicode(KICK))
    );

    public static final String RESIDENT_ROLE_NAME = "resident";
    public static final String RESIDENT_ROLE_COLOR = "2ecc71";
    public static final String FRIEND_ROLE_NAME = "friend";
    public static final String FRIEND_ROLE_COLOR = "1abc9c";
    public static final String VISITOR_ROLE_NAME = "visitor";
    public static final String VISITOR_ROLE_COLOR = "3498db";

    public static final String MAIN_CATEGORY_NAME = "__________🏠__________";
    public static final String MAIN_TEXT_CHANNEL_NAME = "living-room";
    public static final String MAIN_VOICE_CHANNEL_NAME = "couch";

    public static final String ROOM_CATEGORY_NAME = "__________🚪__________";
    public static final String ROOM_DOOR_NAME = "-door";
    public static final String ROOM_DOOR_DEFAULT_TEXT = "``` ```";
    public static final String ROOM_DEFAULT_NAME = "'s room";

    public static String getResidentDoorName(String residentName) {
        return residentName + ROOM_DOOR_NAME;
    }
    public static String getResidentRoomDefaultName(String residentName) {
        return residentName + ROOM_DEFAULT_NAME;
    }
}
