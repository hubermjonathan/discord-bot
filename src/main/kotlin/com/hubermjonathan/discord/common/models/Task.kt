package com.hubermjonathan.discord.common.models

import java.time.Duration
import java.util.TimerTask

abstract class Task(
    val schedule: Duration,
    val delay: Duration = Duration.ZERO,
    protected val featureContext: FeatureContext,
) : TimerTask() {
    protected val jda = featureContext.jda
}
