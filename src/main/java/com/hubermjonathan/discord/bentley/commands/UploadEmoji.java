package com.hubermjonathan.discord.bentley.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class UploadEmoji extends BotOwnerCommand {
    public UploadEmoji(String name, String description) {
        super(
                name,
                Commands.slash(name, description)
                        .addOption(OptionType.ATTACHMENT, "emoji", "the emoji to upload", true)
        );
    }

    @Override
    public void execute() throws Exception {
        Message.Attachment attachment = getEvent().getOption("emoji").getAsAttachment();

        if (!attachment.getFileExtension().equals("png")
                && !attachment.getFileExtension().equals("gif")) {
            throw new Exception();
        }

        getEvent().getGuild().createEmoji(attachment.getFileName().substring(0, attachment.getFileName().length() - 4), attachment.getProxy().downloadAsIcon().join()).queue();
    }
}
