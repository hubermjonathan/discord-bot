package com.hubermjonathan.discordbot.commands;

import com.hubermjonathan.discordbot.Constants;
import com.hubermjonathan.discordbot.models.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;

public class EditRoom extends Command {
    public EditRoom(String command) {
        super(command);
    }

    @Override
    public void execute() throws Exception {
        Category category = null;
        for (TextChannel textChannel : getEvent().getGuild().getTextChannels()) {
            if (textChannel.getName().equals(getEvent().getMember().getEffectiveName() + "s-door")) {
                category = textChannel.getParent();
                break;
            }
        }
        Message roomMessage = category.getTextChannels().get(0).retrievePinnedMessages().complete().get(0);
        EmbedBuilder embedBuilder = new EmbedBuilder(roomMessage.getEmbeds().get(0));
        String[] args = Arrays.copyOfRange(getArgs(), 1, getArgs().length);

        if (getArgs()[0].equals("name")) {
            if (args.length == 0) {
                getEvent().getMessage().addReaction(Constants.DENY).queue();
                throw new Exception();
            }

            embedBuilder.setTitle(String.join(" ", args));
            roomMessage.editMessage(embedBuilder.build()).queue();
            category.getVoiceChannels().get(0).getManager().setName(String.join(" ", args)).queue();
        } else if (getArgs()[0].equals("text")) {
            if (args.length == 0) {
                getEvent().getMessage().addReaction(Constants.DENY).queue();
                throw new Exception();
            }

            embedBuilder.setDescription("```" + String.join(" ", args) + "```");
            roomMessage.editMessage(embedBuilder.build()).queue();
        } else if (getArgs()[0].equals("image")) {
            if ((getEvent().getMessage().getAttachments().size() != 1 && args.length == 0) || (getEvent().getMessage().getAttachments().size() == 1 && !getEvent().getMessage().getAttachments().get(0).isImage())) {
                getEvent().getMessage().addReaction(Constants.DENY).queue();
                throw new Exception();
            }

            if (args.length == 1) {
                embedBuilder.setImage(args[0]);
            } else {
                embedBuilder.setImage(getEvent().getMessage().getAttachments().get(0).getUrl());
            }
            roomMessage.editMessage(embedBuilder.build()).queue();
        }
    }
}
