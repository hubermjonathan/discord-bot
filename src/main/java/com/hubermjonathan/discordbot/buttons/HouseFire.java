package com.hubermjonathan.discordbot.buttons;

import com.hubermjonathan.discordbot.Constants;
import com.hubermjonathan.discordbot.models.Button;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import java.util.EnumSet;
import java.util.Timer;
import java.util.TimerTask;

public class HouseFire extends Button {
    public HouseFire(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        Category category = getEvent().getChannel().getParent();
        TextChannel textChannel = category.getTextChannels().get(0);
        VoiceChannel voiceChannel = category.getVoiceChannels().get(0);

        if (textChannel.getName().contains(Constants.FIRE)) return;

        textChannel.getManager().setName(Constants.FIRE + "-" + textChannel.getName()).queue();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Member member : voiceChannel.getMembers()) {
                    getEvent().getGuild().moveVoiceMember(member, null).queue();
                }

                voiceChannel.getManager()
                        .clearOverridesAdded()
                        .putPermissionOverride(getEvent().getGuild().getPublicRole(), null, EnumSet.of(Permission.VOICE_CONNECT))
                        .putPermissionOverride(getEvent().getMember(), null, EnumSet.of(Permission.VOICE_CONNECT))
                        .queue();
            }
        }, 1000 * 60);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                voiceChannel.getManager()
                        .clearOverridesAdded()
                        .putPermissionOverride(getEvent().getGuild().getPublicRole(), null, EnumSet.of(Permission.VOICE_CONNECT))
                        .putPermissionOverride(getEvent().getMember(), EnumSet.of(Permission.VOICE_CONNECT), null)
                        .queue();

                textChannel.getManager().setName(textChannel.getName().substring(2)).queue();
            }
        }, (1000 * 60) + (1000 * 60 * 25));
    }
}
