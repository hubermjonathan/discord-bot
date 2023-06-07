package com.hubermjonathan.discord.common.models

import com.hubermjonathan.discord.common.DiscordLogger
import net.dv8tion.jda.api.JDABuilder
import java.util.Timer

abstract class Feature(name: String, icon: String) {
    abstract val buttons: List<Button>
    abstract val commands: List<Command>
    abstract val managers: List<Manager>
    abstract val tasks: List<Task>
    protected val context = Context(logger = DiscordLogger(name, icon))

    fun load(jda: JDABuilder) {
        val listenerAdapters = buttons + commands + managers

        listenerAdapters.forEach {
            jda.addEventListeners(it)
        }
    }

    fun startTasks() {
        val timer = Timer()

        tasks.forEach {
            timer.schedule(it, it.delay.toMillis(), it.schedule.toMillis())
        }
    }
}
