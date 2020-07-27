package com.hubermjonathan.mitch.admin;

import com.hubermjonathan.mitch.CommandSet;
import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.GuildManager;

public class AdminCommands extends CommandSet {
    public AdminCommands(MessageReceivedEvent event) {
        super(event);
    }

    public void dispatch() {
        Message message = getEvent().getMessage();
        String content = message.getContentRaw();
        String[] tokens = content.split(" ");

        try {
            switch (tokens[1]) {
                case ("region"):
                    changeRegion(tokens[2]);
                    break;
                case ("priority"):
                    togglePriority();
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            message.addReaction(Constants.DENY).queue();
        }
    }

    private void changeRegion(String region) {
        GuildManager guildManager = getEvent().getMessage().getGuild().getManager();
        switch (region) {
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

        Message message = getEvent().getMessage();
        message.addReaction(Constants.CONFIRM).queue();
    }

    private void togglePriority() {
        VoiceChannel voiceChannel = null;
        for (VoiceChannel vc : getEvent().getGuild().getVoiceChannels()) {
            for (Member m : vc.getMembers()) {
                if (m.getId().equals(getEvent().getAuthor().getId())) {
                    voiceChannel = vc;
                }
            }
        }
        if (voiceChannel == null) {
            Message message = getEvent().getMessage();
            message.addReaction(Constants.NOT_IN_VC).queue();
            return;
        }

        for (Member m : voiceChannel.getMembers()) {
            if (m.isOwner()) continue;
            m.mute(!m.getVoiceState().isGuildMuted()).queue();
        }

        Message message = getEvent().getMessage();
        message.addReaction(Constants.CONFIRM).queue();
    }
}
