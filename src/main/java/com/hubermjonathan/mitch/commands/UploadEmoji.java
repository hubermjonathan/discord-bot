package com.hubermjonathan.mitch.commands;

import com.hubermjonathan.mitch.AdminCommand;
import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public class UploadEmoji extends AdminCommand {
    public UploadEmoji() {
        super("upload", "u");
    }

    @Override
    public void executeCommand() throws Exception {
        if (getEvent().getMessage().getAttachments().size() != 1) {
            getEvent().getMessage().addReaction(Constants.DENY).queue();
            throw new Exception();
        }

        Message message = getEvent().getMessage();
        Message.Attachment attachment = message.getAttachments().get(0);
        Guild guild = getEvent().getGuild();

        if (!attachment.getFileExtension().equals("png")) {
            message.addReaction(Constants.DENY).queue();
            throw new Exception();
        }

        guild.createEmote(attachment.getFileName().substring(0, attachment.getFileName().length() - 4), attachment.retrieveAsIcon().get()).queue();
    }
}
