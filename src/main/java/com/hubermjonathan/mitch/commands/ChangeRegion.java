package com.hubermjonathan.mitch.commands;

import com.hubermjonathan.mitch.Command;
import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.managers.GuildManager;

import java.util.Arrays;

public class ChangeRegion extends Command {
    public ChangeRegion() {
        super("region", Arrays.asList("r"));
    }

    @Override
    public void executeCommand() {
        String newRegion = getArgs()[0];
        GuildManager guildManager = getEvent().getMessage().getGuild().getManager();
        Message message = getEvent().getMessage();

        if (!message.getAuthor().getId().equals(message.getGuild().getOwnerId())) {
            message.addReaction(Constants.DENY).queue();
            return;
        }

        switch (newRegion) {
            case ("w"):
                guildManager.setRegion(Region.US_WEST).queue();
                break;
            case ("e"):
                guildManager.setRegion(Region.US_EAST).queue();
                break;
            case ("c"):
            default:
                guildManager.setRegion(Region.US_CENTRAL).queue();
        }

        message.addReaction(Constants.CONFIRM).queue();
    }
}
