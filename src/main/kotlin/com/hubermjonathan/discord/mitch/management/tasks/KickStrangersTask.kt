package com.hubermjonathan.discord.mitch.management.tasks

import com.hubermjonathan.discord.common.models.Context
import com.hubermjonathan.discord.common.models.Task
import com.hubermjonathan.discord.mitch.guild
import com.hubermjonathan.discord.mitch.strangersRole
import java.time.Duration

private val delay = Duration.ofHours(24)
private val schedule = Duration.ofHours(12)

class KickStrangersTask(context: Context) : Task(delay, schedule, context) {
    private val logger = context.logger

    override fun run() {
        val guild = jda.guild

        guild.getMembersWithRoles(guild.strangersRole).forEach {
            it.kick().queue()
            logger.info("kicked ${it.effectiveName}")
        }
    }
}
