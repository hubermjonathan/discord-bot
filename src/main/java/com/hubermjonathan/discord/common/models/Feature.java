package com.hubermjonathan.discord.common.models;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public abstract class Feature {
    private final boolean enabled;
    private final List<Button> buttons;
    private final List<Command> commands;
    private final List<Manager> managers;
    private final List<Task> tasks;
    private Guild guild;

    public Feature(boolean enabled) {
        this.enabled = enabled;
        this.buttons = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.managers = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void addButton(Button button) {
        buttons.add(button);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public void addManager(Manager manager) {
        managers.add(manager);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Guild getGuild() {
        return guild;
    }

    public void load(JDABuilder jda) {
        if (enabled) {
            create();

            for (Button button : buttons) {
                jda.addEventListeners(button);
            }

            for (Command command : commands) {
                jda.addEventListeners(command);
            }

            for (Manager manager : managers) {
                jda.addEventListeners(manager);
            }
        }
    }

    public void startTasks(Guild guild) {
        if (enabled) {
            this.guild = guild;
            createTasks();

            Timer timer = new Timer();

            for (Task task : tasks) {
                if (task.getStartDate() == null) {
                    timer.schedule(task.getTask(), task.getDelay(), task.getSchedule());
                } else {
                    timer.schedule(task.getTask(), task.getStartDate(), task.getSchedule());
                }
            }
        }
    }

    public abstract void create();
    public abstract void createTasks();
}
