package com.hubermjonathan.discord.mitch.groups.commands

import com.hubermjonathan.discord.common.models.BotOwnerCommand
import com.hubermjonathan.discord.common.models.FeatureContext
import com.hubermjonathan.discord.mitch.groups.publicGroupsCategory
import com.hubermjonathan.discord.mitch.groups.signupSheetTextChannel
import com.hubermjonathan.discord.mitch.purdudesGuild
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands

private const val name = "group"
private val commandData = Commands
    .slash(name, "create a group")
    .addOption(OptionType.STRING, "name", "the name of the group to create", true)

class CreateGroupCommand(featureContext: FeatureContext) : BotOwnerCommand(name, commandData, featureContext) {
    override val allowedChannels = listOf(jda.purdudesGuild.signupSheetTextChannel)

    override fun execute(event: SlashCommandInteractionEvent): String {
        val groupChannelName = event.getOption("name")!!.asString
        val groupChannel = jda.purdudesGuild.publicGroupsCategory
            .createTextChannel(groupChannelName)
            .complete()

        return "created new group ${groupChannel.asMention}"
    }
}
