package com.hubermjonathan.discord.mitch;

import com.hubermjonathan.discord.mitch.buttons.*;
import com.hubermjonathan.discord.mitch.commands.CreateEvent;
import com.hubermjonathan.discord.mitch.commands.EditEvent;
import com.hubermjonathan.discord.mitch.events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;

public class MitchBot {
    public static void run(final String token) throws LoginException, InterruptedException {
        final Delete delete = new Delete("delete");
        final Going going = new Going("going");
        final Maybe maybe = new Maybe("maybe");
        final NotGoing notGoing = new NotGoing("notGoing");

        final CreateEvent createEvent = new CreateEvent("event", "create an event");
        final EditEvent editEvent = new EditEvent("edit", "edit an event");

        final ManageMusicChannel manageMusicChannel = new ManageMusicChannel();
        final ManageTourGroup manageTourGroup = new ManageTourGroup();

        final JDA jda = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(
                        delete, going, maybe, notGoing,
                        createEvent, editEvent,
                        manageMusicChannel, manageTourGroup
                )
                .build();

        jda.awaitReady();

        for (Guild guild : jda.getGuilds()) {
            guild.updateCommands().addCommands(
                    createEvent.getCommandData(),
                    editEvent.getCommandData()
            ).queue();

            Timer timer = new Timer();
            LocalDate now = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST")));
            LocalDateTime nextTime =
                    LocalTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))).isBefore(LocalTime.of(9, 0))
                            ? LocalDateTime.of(now, LocalTime.of(9, 0))
                            : LocalDateTime.of(now.plusDays(1), LocalTime.of(9, 0));
            Date dailyEventsScheduledDate = Date.from(
                    nextTime.atZone(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))).toInstant());

            timer.schedule(new DeleteEvents(guild), 1000 * 60 * 30, 1000 * 60 * 30);
            timer.schedule(new KickTourGroup(guild), 1000 * 60 * 60 * 24, 1000 * 60 * 60 * 12);
            timer.schedule(new SendDailyEvents(guild), dailyEventsScheduledDate, 1000 * 60 * 60 * 24);
            timer.schedule(new SendEventReminder(guild), 0, 1000 * 60);
        }
    }
}
