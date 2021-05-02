package com.hubermjonathan.discordbot.buttons;

import com.hubermjonathan.discordbot.Constants;
import com.hubermjonathan.discordbot.models.Button;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class Kick extends Button {
    public Kick(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        Category category = getEvent().getChannel().getParent();

        if (category.getTextChannels().get(0).getName().contains(Constants.FIRE)) {
            throw new Exception();
        }

        VoiceChannel newVoiceChannel = null;
        for (VoiceChannel voiceChannel : getEvent().getGuild().getVoiceChannels()) {
            if (voiceChannel.getName().equals(Constants.MAIN_VOICE_CHANNEL_NAME)) {
                newVoiceChannel = voiceChannel;
                break;
            }
        }

        for (Member member : category.getVoiceChannels().get(0).getMembers()) {
            if (member.getId().equals(getOwner().getId())) continue;
            getEvent().getGuild().moveVoiceMember(member, newVoiceChannel).queue();
        }
    }
}
