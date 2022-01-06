package com.hubermjonathan.discord.house.button;

import com.hubermjonathan.discord.house.Constants;
import com.hubermjonathan.discord.house.model.GuestButton;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.Button;

public class Knock extends GuestButton {
    public Knock(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        if (!getEvent().getMember().getVoiceState().inVoiceChannel()
                || getEvent().getMember().getVoiceState().getChannel().equals(getEvent().getTextChannel().getParent().getVoiceChannels().get(0))) {
            throw new Exception();
        }

        for (Message message : getEvent().getChannel().getIterableHistory().complete()) {
            for (Member member : message.getMentionedMembers()) {
                if (member.getId().equals(getEvent().getMember().getId())) {
                    throw new Exception();
                }
            }
        }

        // TODO: maybe only allow knock if the owner is in the room

        Member owner = getEvent().getGuild().getMembersByEffectiveName(getEvent().getTextChannel().getName().substring(0, getEvent().getTextChannel().getName().indexOf('-')), true).get(0);
        getEvent().reply("hey " + owner.getAsMention() + ", " + getEvent().getMember().getAsMention() + " is knocking on your door!")
                .addActionRow(
                        Button.secondary(Constants.CONFIRM, Emoji.fromUnicode(Constants.CONFIRM)),
                        Button.secondary(Constants.DENY, Emoji.fromUnicode(Constants.DENY)),
                        Button.secondary(Constants.CANCEL, Emoji.fromUnicode(Constants.CANCEL))
                )
                .queue();
    }
}
