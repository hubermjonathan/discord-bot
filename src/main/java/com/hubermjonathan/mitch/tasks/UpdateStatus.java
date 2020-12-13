package com.hubermjonathan.mitch.tasks;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;

import java.util.Random;
import java.util.TimerTask;

public class UpdateStatus extends TimerTask {
    Presence presence;
    Activity[] activities = {
            Activity.listening("Daniel's flame"),
            Activity.watching("Sammie's stream"),
            Activity.listening("Collin simp"),
            Activity.listening("Aulos Reloaded"),
            Activity.listening("Andy give bad clues"),
            Activity.playing("with my Amazon friends"),
            Activity.listening("the fight song"),
            Activity.playing("against IU"),
            Activity.listening("Jay's sass"),
    };

    public UpdateStatus(Presence presence) {
        this.presence = presence;
    }

    @Override
    public void run() {
        presence.setActivity(activities[new Random().nextInt(activities.length)]);
    }
}
