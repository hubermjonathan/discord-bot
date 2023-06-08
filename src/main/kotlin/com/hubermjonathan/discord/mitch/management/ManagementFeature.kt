package com.hubermjonathan.discord.mitch.management

import com.hubermjonathan.discord.common.models.Button
import com.hubermjonathan.discord.common.models.Feature
import com.hubermjonathan.discord.mitch.management.commands.MakeFriendCommand
import com.hubermjonathan.discord.mitch.management.commands.UploadEmojiCommand
import com.hubermjonathan.discord.mitch.management.managers.StrangersManager
import com.hubermjonathan.discord.mitch.management.managers.ThreadManager
import com.hubermjonathan.discord.mitch.management.tasks.KickStrangersTask
import net.dv8tion.jda.api.JDA

private const val name = "management"
private const val icon = "\uD83D\uDC64"

class ManagementFeature(jda: JDA) : Feature(jda, name, icon) {
    override val buttons = emptyList<Button>()

    override val commands = listOf(
        MakeFriendCommand(context),
        UploadEmojiCommand(context),
    )

    override val managers = listOf(
        StrangersManager(context),
        ThreadManager(context),
    )

    override val tasks = listOf(
        KickStrangersTask(context),
    )
}
