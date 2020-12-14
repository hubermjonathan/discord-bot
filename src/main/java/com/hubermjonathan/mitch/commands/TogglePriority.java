package com.hubermjonathan.mitch.commands;

import com.hubermjonathan.mitch.AdminCommand;
import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Arrays;

public class TogglePriority extends AdminCommand {
    public TogglePriority() {
        super("priority", Arrays.asList("p"));
    }

    @Override
    public void executeCommand() throws Exception {
        VoiceChannel voiceChannel = getEvent().getMember().getVoiceState().getChannel();
        Message message = getEvent().getMessage();

        if (voiceChannel == null) {
            message.addReaction(Constants.NOT_IN_VC).queue();
            throw new Exception();
        }

        for (Member m : voiceChannel.getMembers()) {
            if (m.isOwner()) continue;
            m.mute(!m.getVoiceState().isGuildMuted()).queue();
        }

        message.addReaction(Constants.CONFIRM).queue();
    }
}
