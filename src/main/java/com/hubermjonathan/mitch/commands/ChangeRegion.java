package com.hubermjonathan.mitch.commands;

import com.hubermjonathan.mitch.AdminCommand;
import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.managers.GuildManager;

public class ChangeRegion extends AdminCommand {
    public ChangeRegion() {
        super("region", "r");
    }

    @Override
    public void executeCommand() throws Exception {
        if (getArgs().length != 1) {
            getEvent().getMessage().addReaction(Constants.DENY).queue();
            throw new Exception();
        }

        String newRegion = getArgs()[0];
        GuildManager guildManager = getEvent().getMessage().getGuild().getManager();

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
    }
}
