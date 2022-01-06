package com.hubermjonathan.discord.house.command;

import com.hubermjonathan.discord.house.Constants;
import com.hubermjonathan.discord.house.model.ResidentCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class EditRoom extends ResidentCommand {
    public EditRoom(String name, String description) {
        super(
                name,
                new CommandData(name, description).addSubcommands(
                        new SubcommandData("name", "edit the name of your room").addOption(
                                OptionType.STRING, "value", "the new name for your room", true
                        ),
                        new SubcommandData("text", "edit the text of your room").addOption(
                                OptionType.STRING, "value", "the new text for your room", true
                        ),
                        new SubcommandData("image", "edit the image of your room").addOption(
                                OptionType.STRING, "value", "the new image for your room", true
                        )
                )
        );
    }

    @Override
    public void execute() throws Exception {
        Category roomCategory = getEvent().getGuild().getTextChannelsByName(Constants.getResidentDoorName(getEvent().getMember().getEffectiveName()), true).get(0).getParent();
        Message doorMessage = roomCategory.getTextChannels().get(0).retrievePinnedMessages().complete().get(0);
        EmbedBuilder embedBuilder = new EmbedBuilder(doorMessage.getEmbeds().get(0));

        if (getEvent().getSubcommandName().equals("name")) {
            embedBuilder.setTitle(getEvent().getOption("value").getAsString());
            doorMessage.editMessageEmbeds(embedBuilder.build()).queue();
            roomCategory.getVoiceChannels().get(0).getManager().setName(getEvent().getOption("value").getAsString()).queue();
        } else if (getEvent().getSubcommandName().equals("text")) {
            embedBuilder.setDescription("```" + getEvent().getOption("value").getAsString() + "```");
            doorMessage.editMessageEmbeds(embedBuilder.build()).queue();
        } else if (getEvent().getSubcommandName().equals("image")) {
            embedBuilder.setImage(getEvent().getOption("value").getAsString());
            doorMessage.editMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}
