package com.hubermjonathan.discord.mitch.management.tasks

import com.hubermjonathan.discord.common.models.FeatureContext
import com.hubermjonathan.discord.common.models.Task
import com.hubermjonathan.discord.mitch.management.strangersRole
import com.hubermjonathan.discord.mitch.purdudesGuild
import java.time.Duration

private val delay = Duration.ofHours(12)
private val schedule = Duration.ofHours(24)

class KickStrangersTask(featureContext: FeatureContext) : Task(delay, schedule, featureContext) {
    private val logger = featureContext.logger

    override fun run() {
        val guild = jda.purdudesGuild

        guild
            .getMembersWithRoles(guild.strangersRole)
            .forEach {
                logger.info("kicked ${it.effectiveName} from the server")
                it
                    .kick()
                    .queue()
            }
    }
}
