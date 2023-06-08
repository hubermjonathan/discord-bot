package com.hubermjonathan.discord.mitch.groups.buttons

import com.hubermjonathan.discord.common.models.Button
import com.hubermjonathan.discord.common.models.FeatureContext
import com.hubermjonathan.discord.common.models.InteractionException
import com.hubermjonathan.discord.mitch.purdudesGuild
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent

class JoinOrLeaveGroupButton(private val groupChannelId: String, featureContext: FeatureContext) : Button(groupChannelId, featureContext) {
    private val logger = featureContext.logger

    override fun execute(event: ButtonInteractionEvent) {
        val groupChannel = jda.purdudesGuild.getTextChannelById(groupChannelId)
            ?: throw InteractionException(
                "group button does not have a matching group channel",
                event.user,
                groupChannelId,
                featureContext,
            )
        val memberPermissionOverride = groupChannel.memberPermissionOverrides.find { it.member == event.member }
        val memberIsInGroup = memberPermissionOverride != null

        if (memberIsInGroup) {
            groupChannel.manager
                .removePermissionOverride(memberPermissionOverride?.permissionHolder!!)
                .queue()
            logger.info("${event.member?.effectiveName} left ${groupChannel.asMention}")
        } else {
            groupChannel.manager
                .putMemberPermissionOverride(event.member!!.idLong, setOf(Permission.VIEW_CHANNEL), null)
                .queue()
            logger.info("${event.member?.effectiveName} joined ${groupChannel.asMention}")
        }
    }
}
