package com.hubermjonathan.mitch.admin;

import com.hubermjonathan.mitch.Commands;
import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.GuildManager;

public class AdminCommands extends Commands {
    public AdminCommands(MessageReceivedEvent event) {
        super(event);
    }

    @Override
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
                default:
                    message.addReaction(Constants.DENY).queue();
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
        Guild guild = getEvent().getGuild();
        VoiceChannel voiceChannel = null;
        for (VoiceChannel vc : guild.getVoiceChannels()) {
            if (vc.getMembers().contains(guild.getOwner())) {
                voiceChannel = vc;
                break;
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
