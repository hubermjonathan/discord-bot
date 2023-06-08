package com.hubermjonathan.discord.common.models

import com.hubermjonathan.discord.common.DiscordLogger
import net.dv8tion.jda.api.JDA
import java.util.Timer

abstract class Feature(jda: JDA, name: String, icon: String) {
    abstract val buttons: List<Button>
    abstract val commands: List<Command>
    abstract val managers: List<Manager>
    abstract val tasks: List<Task>
    protected val context = FeatureContext(jda, logger = DiscordLogger(jda, name, icon), name, icon)

    fun load(jda: JDA) {
        val listenerAdapters = buttons + commands + managers

        listenerAdapters.forEach {
            jda.addEventListener(it)
        }
        managers.forEach {
            it.whenLoaded()
        }
    }

    fun startTasks() {
        val timer = Timer()

        tasks.forEach {
            timer.schedule(it, it.delay.toMillis(), it.schedule.toMillis())
        }
    }
}
