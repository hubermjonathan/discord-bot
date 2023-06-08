package com.hubermjonathan.discord.mitch.management.commands

import com.hubermjonathan.discord.common.models.Command
import com.hubermjonathan.discord.common.models.FeatureContext
import com.hubermjonathan.discord.mitch.management.friendsRole
import com.hubermjonathan.discord.mitch.management.strangersRole
import com.hubermjonathan.discord.mitch.purdudesGuild
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands

private const val name = "friend"
private val commandData = Commands
    .slash(name, "make a stranger a friend")
    .addOption(OptionType.USER, "stranger", "the stranger to make a friend", true)

class MakeFriendCommand(featureContext: FeatureContext) : Command(name, commandData, featureContext) {
    private val logger = featureContext.logger

    override fun execute(event: SlashCommandInteractionEvent): String {
        val guild = jda.purdudesGuild
        val stranger = event.getOption("stranger")!!.asMember
            ?: throw IllegalArgumentException("${event.getOption("stranger")!!.asUser.name} is not a member of this server")

        if (!stranger.roles.contains(guild.strangersRole)) {
            throw IllegalArgumentException("${stranger.effectiveName} is not a stranger")
        }

        if (stranger.roles.contains(guild.friendsRole)) {
            throw IllegalArgumentException("${stranger.effectiveName} is already a friend")
        }

        guild
            .addRoleToMember(stranger, guild.friendsRole)
            .queue()
        guild
            .removeRoleFromMember(stranger, guild.strangersRole)
            .queue()
        logger.info("${event.member?.asMention} made ${stranger.asMention} a friend")

        return "made ${stranger.asMention} a friend"
    }
}
