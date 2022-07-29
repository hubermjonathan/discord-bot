package com.hubermjonathan.discord.bentley.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UploadEmoji extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().getId().equals(System.getenv("BOT_OWNER_ID")) || event.getMessage().getAttachments().size() != 1 || !event.isFromGuild()) {
            return;
        }

        Message message = event.getMessage();
        Message.Attachment attachment = message.getAttachments().get(0);
        Guild guild = event.getGuild();
        String emojiName = attachment.getFileName().substring(0, attachment.getFileName().length() - 4);

        if (!attachment.getFileExtension().equals("png") && !attachment.getFileExtension().equals("gif")) {
            return;
        }

        RichCustomEmoji emoji = guild.createEmoji(emojiName, attachment.getProxy().downloadAsIcon().join()).complete();
        event.getMessage().addReaction(Emoji.fromCustom(emoji)).complete();
    }
}
