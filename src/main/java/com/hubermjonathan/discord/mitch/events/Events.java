package com.hubermjonathan.discord.mitch.events;

import com.hubermjonathan.discord.mitch.MitchConstants;
import com.hubermjonathan.discord.mitch.events.buttons.DeleteEvent;
import com.hubermjonathan.discord.mitch.events.buttons.RSVPGoing;
import com.hubermjonathan.discord.mitch.events.buttons.RSVPMaybe;
import com.hubermjonathan.discord.mitch.events.buttons.RSVPNotGoing;
import com.hubermjonathan.discord.mitch.events.commands.CreateEvent;
import com.hubermjonathan.discord.mitch.events.commands.EditEvent;
import com.hubermjonathan.discord.mitch.events.events.DeleteEvents;
import com.hubermjonathan.discord.mitch.events.events.SendDailyEvents;
import com.hubermjonathan.discord.mitch.events.events.SendEventReminder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;

public class Events {
    public static void load(final JDABuilder jda) {
        final DeleteEvent deleteEvent = new DeleteEvent("deleteEvent");
        final RSVPGoing rsvpGoing = new RSVPGoing("rsvpGoing");
        final RSVPMaybe rsvpMaybe = new RSVPMaybe("rsvpMaybe");
        final RSVPNotGoing rsvpNotGoing = new RSVPNotGoing("rsvpNotGoing");

        jda.addEventListeners(deleteEvent, rsvpGoing, rsvpMaybe, rsvpNotGoing);

    }

    public static void loadDeferred(final JDA jda) {
        final Guild guild = jda.getGuildById(MitchConstants.SERVER_ID);
        final Timer timer = new Timer();

        final CreateEvent createEvent = new CreateEvent("event", "create an event");
        final EditEvent editEvent = new EditEvent("edit", "edit an event");

        guild
                .updateCommands()
                .addCommands(
                        createEvent.getCommandData(),
                        editEvent.getCommandData()
                )
                .queue();

        final LocalDate now = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST")));
        final LocalDateTime nextTime =
                LocalTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))).isBefore(LocalTime.of(9, 0))
                        ? LocalDateTime.of(now, LocalTime.of(9, 0))
                        : LocalDateTime.of(now.plusDays(1), LocalTime.of(9, 0));
        final Date dailyEventsScheduledDate = Date.from(
                nextTime.atZone(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))).toInstant());

        timer.schedule(new DeleteEvents(guild), 1000 * 60 * 30, 1000 * 60 * 30);
        timer.schedule(new SendDailyEvents(guild), dailyEventsScheduledDate, 1000 * 60 * 60 * 24);
        timer.schedule(new SendEventReminder(guild), 0, 1000 * 60);
    }
}
