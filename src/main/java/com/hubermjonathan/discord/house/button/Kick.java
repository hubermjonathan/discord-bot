package com.hubermjonathan.discord.house.button;

import com.hubermjonathan.discord.house.Constants;
import com.hubermjonathan.discord.house.model.Button;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class Kick extends Button {
    public Kick(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        VoiceChannel newVoiceChannel = getEvent().getGuild().getVoiceChannelsByName(Constants.MAIN_VOICE_CHANNEL_NAME, true).get(0);
        Member owner = getEvent().getGuild().getMembersByEffectiveName(getEvent().getTextChannel().getName().substring(0, getEvent().getTextChannel().getName().indexOf('-')), true).get(0);

        for (Member member : getEvent().getTextChannel().getParentCategory().getVoiceChannels().get(0).getMembers()) {
            if (!member.getId().equals(owner.getId())) {
                getEvent().getGuild().moveVoiceMember(member, newVoiceChannel).queue();
            }
        }

        getEvent().deferEdit().queue();
    }
}
