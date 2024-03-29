package com.hubermjonathan.discord.mitch.management.commands

import com.hubermjonathan.discord.common.models.Command
import com.hubermjonathan.discord.common.models.FeatureContext
import com.hubermjonathan.discord.common.models.InteractionException
import com.hubermjonathan.discord.mitch.purdudesGuild
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands

private const val name = "emoji"
private val commandData = Commands
    .slash(name, "upload an emoji")
    .addOption(OptionType.STRING, "name", "the name of the emoji to upload", true)
    .addOption(OptionType.ATTACHMENT, "image", "the image of the emoji to upload", true)
private val allowedFileExtensions = listOf("png", "gif")

class UploadEmojiCommand(featureContext: FeatureContext) : Command(name, commandData, featureContext) {
    private val logger = featureContext.logger

    override fun execute(event: SlashCommandInteractionEvent): String {
        val guild = jda.purdudesGuild
        val emojiName = event.getOption("name")!!.asString
        val attachment = event.getOption("image")!!.asAttachment
        val fileExtension = attachment.fileExtension

        if (emojiName.contains(' ')) {
            throw InteractionException(
                "name must be one word",
                event.user,
                name,
                featureContext,
            )
        }

        if (fileExtension == null || fileExtension !in allowedFileExtensions) {
            throw InteractionException(
                "image must be one of the allowed types: `${allowedFileExtensions.map { ".$it" }}`",
                event.user,
                name,
                featureContext,
            )
        }

        val emoji = guild
            .createEmoji(emojiName, attachment.proxy.downloadAsIcon().get())
            .complete()

        logger.info("${event.member?.asMention} uploaded an emoji '$emojiName' <:${emoji.name}:${emoji.id}>")

        return "uploaded '$emojiName' <:${emoji.name}:${emoji.id}>"
    }
}
