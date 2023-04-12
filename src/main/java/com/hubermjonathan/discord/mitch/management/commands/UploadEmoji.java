package com.hubermjonathan.discord.mitch.management.commands;

import com.hubermjonathan.discord.common.Logger;
import com.hubermjonathan.discord.common.models.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class UploadEmoji extends Command {
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

        String emojiName = attachment.getFileName().substring(0, attachment.getFileName().length() - 4);

        Logger.log(
                getEvent().getJDA(),
                "\uD83D\uDC64 management",
                String.format(
                        "%s uploaded an emoji %s",
                        getEvent().getMember().getEffectiveName(),
                        emojiName
                )
        );
        getEvent()
                .getGuild()
                .createEmoji(
                        emojiName,
                        attachment.getProxy().downloadAsIcon().join()
                )
                .queue();
    }
}
