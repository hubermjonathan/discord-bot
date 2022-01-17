package com.hubermjonathan.discord.house.button;

import com.hubermjonathan.discord.house.Constants;
import com.hubermjonathan.discord.house.model.GuestButton;
import com.hubermjonathan.discord.house.player.PlayerManager;
import net.dv8tion.jda.api.entities.Member;

public class Poke extends GuestButton {
    public Poke(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        if (!getEvent().getMember().getVoiceState().inVoiceChannel()
                || getEvent().getGuild().getSelfMember().getVoiceState().inVoiceChannel()) {
            throw new Exception();
        }

        Member owner = getEvent().getGuild().getMembersByEffectiveName(getEvent().getTextChannel().getName().substring(0, getEvent().getTextChannel().getName().indexOf('-')), true).get(0);
        if (owner.getVoiceState().inVoiceChannel()
                && owner.getVoiceState().getChannel().equals(getEvent().getTextChannel().getParent().getVoiceChannels().get(0))) {
            owner.getUser().openPrivateChannel().complete().sendMessage("hey " + owner.getAsMention() + ", " + getEvent().getMember().getAsMention() + " poked you!").queue();
            getEvent().deferEdit().queue();
            PlayerManager.getInstance().loadAndPlay(getEvent().getGuild(), owner.getVoiceState().getChannel(), Constants.POKE_SOUND);
        }
    }
}
