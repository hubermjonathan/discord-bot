package com.hubermjonathan.discord.mitch.events;

import com.hubermjonathan.discord.common.models.Feature;
import com.hubermjonathan.discord.mitch.events.buttons.DeleteEvent;
import com.hubermjonathan.discord.mitch.events.buttons.RSVPGoing;
import com.hubermjonathan.discord.mitch.events.buttons.RSVPMaybe;
import com.hubermjonathan.discord.mitch.events.buttons.RSVPNotGoing;
import com.hubermjonathan.discord.mitch.events.commands.CreateEvent;
import com.hubermjonathan.discord.mitch.events.commands.EditEvent;
import com.hubermjonathan.discord.mitch.events.tasks.DeleteEvents;
import com.hubermjonathan.discord.mitch.events.tasks.SendDailyEvents;
import com.hubermjonathan.discord.mitch.events.tasks.SendEventReminder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class Events extends Feature {
    public Events(boolean enabled) {
        super(enabled);
    }

    @Override
    public void create() {
        addButton(new DeleteEvent("deleteEvent"));
        addButton(new RSVPGoing("rsvpGoing"));
        addButton(new RSVPMaybe("rsvpMaybe"));
        addButton(new RSVPNotGoing("rsvpNotGoing"));

        addCommand(new CreateEvent("event", "create an event"));
        addCommand(new EditEvent("edit", "edit an event"));
    }

    @Override
    public void createTasks() {
        LocalDate now = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST")));
        LocalDateTime nextTime =
                LocalTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))).isBefore(LocalTime.of(9, 0))
                        ? LocalDateTime.of(now, LocalTime.of(9, 0))
                        : LocalDateTime.of(now.plusDays(1), LocalTime.of(9, 0));
        Date dailyEventsScheduledDate = Date.from(
                nextTime.atZone(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))).toInstant());

        addTask(new DeleteEvents(getGuild(), 1000 * 60 * 30, 1000 * 60 * 30));
        addTask(new SendDailyEvents(getGuild(), dailyEventsScheduledDate, 1000 * 60 * 60 * 24));
        addTask(new SendEventReminder(getGuild(), 0, 1000 * 60));
    }
}
