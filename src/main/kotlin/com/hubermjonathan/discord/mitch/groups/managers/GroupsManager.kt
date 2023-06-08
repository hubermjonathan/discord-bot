package com.hubermjonathan.discord.mitch.groups.managers

import com.hubermjonathan.discord.common.Util.buildMessageEmbed
import com.hubermjonathan.discord.common.models.FeatureContext
import com.hubermjonathan.discord.common.models.Manager
import com.hubermjonathan.discord.mitch.groups.archivedGroupsCategory
import com.hubermjonathan.discord.mitch.groups.buttons.JoinOrLeaveGroupButton
import com.hubermjonathan.discord.mitch.groups.publicGroupsCategory
import com.hubermjonathan.discord.mitch.groups.signupSheetTextChannel
import com.hubermjonathan.discord.mitch.purdudesGuild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.unions.ChannelUnion
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateParentEvent
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import net.dv8tion.jda.api.utils.messages.MessageData
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder
import net.dv8tion.jda.api.utils.messages.MessageEditData

class GroupsManager(featureContext: FeatureContext) : Manager(featureContext) {
    private val groupButtons = mutableMapOf<TextChannel, JoinOrLeaveGroupButton>()

    override fun whenLoaded() {
        createOrUpdateGroupsMessage()
    }

    override fun onChannelCreate(event: ChannelCreateEvent) {
        createOrUpdateGroupsMessageIfInGroupChannel(event.channel)
    }

    override fun onChannelUpdateParent(event: ChannelUpdateParentEvent) {
        createOrUpdateGroupsMessageIfInGroupChannel(event.channel)
    }

    override fun onChannelUpdateName(event: ChannelUpdateNameEvent) {
        createOrUpdateGroupsMessageIfInGroupChannel(event.channel)
    }

    private fun createOrUpdateGroupsMessageIfInGroupChannel(channel: ChannelUnion) {
        if (channel !is TextChannel) return

        val isPublicGroupChannel = channel.parentCategory == jda.purdudesGuild.publicGroupsCategory
        val isArchivedGroupChannel = channel.parentCategory == jda.purdudesGuild.archivedGroupsCategory

        if (!isPublicGroupChannel && !isArchivedGroupChannel) return

        createOrUpdateGroupsMessage()
    }

    private fun createOrUpdateGroupsMessage() {
        val groupChannels = jda.purdudesGuild.publicGroupsCategory.textChannels.filter {
            it != jda.purdudesGuild.signupSheetTextChannel
        }
        val groupButtonRows = getGroupButtonRows(groupChannels)
        val signupSheetMessage = jda.purdudesGuild.signupSheetTextChannel
            .retrieveMessageById(jda.purdudesGuild.signupSheetTextChannel.latestMessageId)
            .complete()

        groupButtons.filter {
            it.key !in groupChannels
        }.forEach {
            groupButtons.remove(it.key)
            jda.removeEventListener(it.value)
        }

        groupChannels.filter {
            it !in groupButtons
        }.forEach {
            val groupButton = JoinOrLeaveGroupButton(it.id, featureContext)

            groupButtons[it] = groupButton
            jda.addEventListener(groupButton)
        }

        if (signupSheetMessage == null) {
            jda.purdudesGuild.signupSheetTextChannel
                .sendMessage(buildGroupMessage(null, groupButtonRows) as MessageCreateData)
                .queue()
        } else {
            signupSheetMessage
                .editMessage(buildGroupMessage(signupSheetMessage, groupButtonRows) as MessageEditData)
                .queue()
        }
    }

    private fun getGroupButtonRows(groupChannels: List<TextChannel>): List<ActionRow> {
        val buttons = groupChannels.map {
            val emoji = it.name.split("-").first()
            val name = it.name.split("-").drop(1).joinToString(" ")

            Button.secondary(it.id, "$emoji $name")
        }

        return buttons.chunked(2).map {
            ActionRow.of(it)
        }
    }

    private fun buildGroupMessage(message: Message?, actionRows: List<ActionRow>): MessageData {
        val messageBuilder = if (message != null) {
            MessageEditBuilder()
        } else {
            MessageCreateBuilder()
        }

        messageBuilder.setEmbeds(buildMessageEmbed("${featureContext.icon} ${featureContext.name}", "click the buttons below to join or leave a group"))
        messageBuilder.setComponents(actionRows)

        return messageBuilder.build()
    }
}
