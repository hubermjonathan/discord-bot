package com.hubermjonathan.discord.house.button;

import com.hubermjonathan.discord.house.Constants;
import com.hubermjonathan.discord.house.model.ResidentButton;
import com.hubermjonathan.discord.house.player.PlayerManager;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Poke extends ResidentButton {
    public Poke(String name) {
        super(
                name,
                Commands.user(name)
        );
    }

    @Override
    public String execute() throws Exception {
        if (!getEvent().getTargetMember().getRoles().get(0).getName().equals(Constants.RESIDENT_ROLE_NAME)
                || getEvent().getMember().equals(getEvent().getTargetMember())
                || getEvent().getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
            throw new Exception();
        }

        getEvent().getTargetMember().getUser().openPrivateChannel().complete().sendMessage(getEvent().getMember().getAsMention() + " poked you! " + Emoji.fromUnicode(Constants.POKE).getName()).queue();

        if (getEvent().getTargetMember().getVoiceState().inAudioChannel()) {
            PlayerManager.getInstance().loadAndPlay(getEvent().getGuild(), getEvent().getTargetMember().getVoiceState().getChannel(), Constants.POKE_SOUND_FEMALE);
        }

        return "you poked " + getEvent().getTargetMember().getAsMention() + "! " + Emoji.fromUnicode(Constants.POKE).getName();
    }
}
