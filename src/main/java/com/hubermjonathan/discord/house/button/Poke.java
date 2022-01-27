package com.hubermjonathan.discord.house.button;

import com.hubermjonathan.discord.house.Constants;
import com.hubermjonathan.discord.house.model.ResidentButton;
import com.hubermjonathan.discord.house.player.PlayerManager;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Poke extends ResidentButton {
    public Poke(String name) {
        super(
                name,
                Commands.user(name)
        );
    }

    @Override
    public void execute() throws Exception {
//        if (!getEvent().getMember().getVoiceState().inAudioChannel()
//                || getEvent().getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
//            throw new Exception();
//        }

        if (getEvent().getTargetMember().getVoiceState().inAudioChannel()) {
            getEvent().getTargetMember().getUser().openPrivateChannel().complete().sendMessage(getEvent().getMember().getAsMention() + " poked you!").queue();
            PlayerManager.getInstance().loadAndPlay(getEvent().getGuild(), getEvent().getTargetMember().getVoiceState().getChannel(), Constants.POKE_SOUND_FEMALE);
        }
    }
}
