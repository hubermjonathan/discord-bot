package com.hubermjonathan.discord.mitch.groups.commands

import com.hubermjonathan.discord.common.models.BotOwnerCommand
import com.hubermjonathan.discord.common.models.Context
import com.hubermjonathan.discord.mitch.groups.archivedGroupsCategory
import com.hubermjonathan.discord.mitch.groups.signupSheetTextChannel
import com.hubermjonathan.discord.mitch.purdudesGuild
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import java.lang.IllegalArgumentException

private const val name = "archive"
private val commandData = Commands
    .slash("archive", "archive a group")
    .addOption(OptionType.CHANNEL, "channel", "group channel to archive", true)

class ArchiveGroupCommand(context: Context) : BotOwnerCommand(name, commandData, context) {
    override val allowedChannels = listOf(jda.purdudesGuild.signupSheetTextChannel)

    override fun execute(event: SlashCommandInteractionEvent): String {
        val channelToArchive = try {
            event.getOption("channel")!!.asChannel.asTextChannel()
        } catch (e: IllegalStateException) {
            throw IllegalArgumentException("group must be a text channel")
        }

        channelToArchive.manager
            .setParent(jda.purdudesGuild.archivedGroupsCategory)
            .sync()
            .complete()

        return "archived ${channelToArchive.asMention}"
    }
}