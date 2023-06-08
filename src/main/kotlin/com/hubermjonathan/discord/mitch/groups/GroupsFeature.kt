package com.hubermjonathan.discord.mitch.groups

import com.hubermjonathan.discord.common.models.Button
import com.hubermjonathan.discord.common.models.Feature
import com.hubermjonathan.discord.common.models.Task
import com.hubermjonathan.discord.mitch.groups.commands.ArchiveGroupCommand
import com.hubermjonathan.discord.mitch.groups.commands.CreateGroupCommand
import com.hubermjonathan.discord.mitch.groups.managers.GroupsManager

private const val name = "groups"
private const val icon = "\uD83D\uDC65"

class GroupsFeature : Feature(name, icon) {
    override val buttons = emptyList<Button>()

    override val commands = listOf(
        ArchiveGroupCommand(context),
        CreateGroupCommand(context),
    )

    override val managers = listOf(
        GroupsManager(context),
    )

    override val tasks = emptyList<Task>()
}
