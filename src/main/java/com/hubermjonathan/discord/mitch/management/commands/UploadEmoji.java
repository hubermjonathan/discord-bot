package com.hubermjonathan.discord.mitch.management.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class UploadEmoji extends BotOwnerCommand {
    public UploadEmoji() {
        super(
                "emoji",
                Commands
                        .slash("emoji", "upload an emoji")
                        .addOption(OptionType.ATTACHMENT, "image", "the emoji to upload", true),
                null
        );
    }

    @Override
    public void execute() throws Exception {
        Message.Attachment attachment = getEvent()
                .getOption("image")
                .getAsAttachment();

        if (!attachment.getFileExtension().equals("png") && !attachment.getFileExtension().equals("gif")) {
            throw new Exception();
        }

        getEvent()
                .getGuild()
                .createEmoji(
                        attachment.getFileName().substring(0, attachment.getFileName().length() - 4),
                        attachment.getProxy().downloadAsIcon().join()
                )
                .queue();
    }
}
