package com.hubermjonathan.discord.common.models;

import net.dv8tion.jda.api.entities.Guild;

import java.util.Date;
import java.util.TimerTask;

public abstract class Task {
    private final Guild guild;
    private final long delay;
    private final Date startDate;
    private final long schedule;
    private final TimerTask task;

    public Task(Guild guild, long delay, long schedule) {
        this.guild = guild;
        this.delay = delay;
        this.startDate = null;
        this.schedule = schedule;
        this.task = new TimerTask() {
            @Override
            public void run() {
                execute();
            }
        };
    }

    public Task(Guild guild, Date startDate, long schedule) {
        this.guild = guild;
        this.delay = -1;
        this.startDate = startDate;
        this.schedule = schedule;
        this.task = new TimerTask() {
            @Override
            public void run() {
                execute();
            }
        };
    }

    public Guild getGuild() {
        return guild;
    }

    public TimerTask getTask() {
        return task;
    }

    public long getDelay() {
        return delay;
    }

    public Date getStartDate() {
        return startDate;
    }

    public long getSchedule() {
        return schedule;
    }

    public abstract void execute();
}
