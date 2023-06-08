package com.hubermjonathan.discord.mitch.management.commands

import com.hubermjonathan.discord.common.models.Command
import com.hubermjonathan.discord.common.models.Context
import com.hubermjonathan.discord.mitch.purdudesGuild
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands

private const val name = "emoji"
private val commandData = Commands
    .slash(name, "upload an emoji")
    .addOption(OptionType.ATTACHMENT, "image", "the emoji to upload", true)
private val allowedFileExtensions = listOf("png", "gif")

class UploadEmojiCommand(context: Context) : Command(name, commandData, context) {
    private val logger = context.logger

    override fun execute(event: SlashCommandInteractionEvent): String {
        val guild = jda.purdudesGuild
        val attachment = event.getOption("image")!!.asAttachment
        val fileExtension = attachment.fileExtension

        if (fileExtension == null || fileExtension !in allowedFileExtensions) {
            throw IllegalArgumentException("emoji file extension must be one of $allowedFileExtensions")
        }

        val emojiName = attachment.fileName.removeSuffix(".$fileExtension")
        val emoji = guild
            .createEmoji(emojiName, attachment.proxy.downloadAsIcon().get())
            .complete()

        logger.info("${event.member?.asMention} uploaded an emoji '$emojiName' <:${emoji.name}:${emoji.id}>")

        return "uploaded '$emojiName' <:${emoji.name}:${emoji.id}>"
    }
}
