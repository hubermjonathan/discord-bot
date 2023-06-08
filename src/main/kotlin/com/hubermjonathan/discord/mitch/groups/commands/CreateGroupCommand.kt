package com.hubermjonathan.discord.mitch.groups.commands

import com.hubermjonathan.discord.common.models.BotOwnerCommand
import com.hubermjonathan.discord.common.models.Context
import com.hubermjonathan.discord.mitch.groups.publicGroupsCategory
import com.hubermjonathan.discord.mitch.groups.signupSheetTextChannel
import com.hubermjonathan.discord.mitch.purdudesGuild
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import java.lang.IllegalArgumentException

private const val name = "group"
private val commandData = Commands
    .slash("group", "create a group")
    .addOption(OptionType.STRING, "name", "the name of the group to create", true)

class CreateGroupCommand(context: Context) : BotOwnerCommand(name, commandData, context) {
    override val allowedChannels = listOf(jda.purdudesGuild.signupSheetTextChannel)

    override fun execute(event: SlashCommandInteractionEvent): String {
        val groupChannelName = event.getOption("name")!!.asString
        val regex = "[\\p{L}\\p{N}\\p{P}\\p{Z}]"
        val groupChannelIcon = groupChannelName.replace(regex.toRegex(), "")

        if (groupChannelIcon.length != 1 || groupChannelName.indexOf(groupChannelIcon) != 0) {
            throw IllegalArgumentException("group name must be correct, guess how")
        }

        val groupChannel = jda.purdudesGuild.publicGroupsCategory
            .createTextChannel(groupChannelName)
            .complete()

        return "created new group ${groupChannel.asMention}"
    }
}
