package com.hubermjonathan.discord.common.models

import com.hubermjonathan.discord.common.DiscordLogger
import net.dv8tion.jda.api.JDA
import java.util.Timer

abstract class Feature(private val jda: JDA, private val name: String, icon: String) {
    abstract val buttons: List<Button>
    abstract val commands: List<Command>
    abstract val managers: List<Manager>
    abstract val tasks: List<Task>
    protected val context = FeatureContext(jda, logger = DiscordLogger(jda, name, icon), name, icon)

    fun load() {
        val timer = Timer()
        val listenerAdapters = buttons + commands + managers

        commands.forEach { command ->
            jda.guilds.forEach { guild ->
                guild.upsertCommand(command.commandData)
            }
        }
        listenerAdapters.forEach {
            jda.addEventListener(it)
        }
        managers.forEach {
            it.whenLoaded()
        }
        tasks.forEach {
            timer.schedule(it, it.delay.toMillis(), it.schedule.toMillis())
        }
    }

    override fun toString() = this.name
}
