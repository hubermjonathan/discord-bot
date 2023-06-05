package com.hubermjonathan.discord.common.models

import net.dv8tion.jda.api.JDA
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Duration
import java.util.TimerTask

abstract class Task(
    val schedule: Duration,
    val delay: Duration = Duration.ZERO,
    protected val context: Context,
) : TimerTask(), KoinComponent {
    protected val jda: JDA by inject()
}
