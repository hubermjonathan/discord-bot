package com.hubermjonathan.discordbot.models;

import com.hubermjonathan.discordbot.Constants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class Button extends ListenerAdapter {
    private final String name;
    private final boolean isOwnerControlled;
    private Member owner;
    private GuildMessageReactionAddEvent event;

    public Button(String name) {
        this.name = name;
        this.isOwnerControlled = true;
    }

    public Button(String name, boolean isOwnerControlled) {
        this.name = name;
        this.isOwnerControlled = isOwnerControlled;
    }

    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public GuildMessageReactionAddEvent getEvent() {
        return event;
    }

    public void setEvent(GuildMessageReactionAddEvent event) {
        this.event = event;
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        User user = event.getUser();
        TextChannel textChannel = event.getChannel();
        MessageReaction messageReaction = event.getReaction();
        String emoji = event.getReactionEmote().getEmoji();
        Member member = event.getMember();

        if (user.isBot() || !textChannel.getParent().getName().equals(Constants.ROOMS_CATEGORY_NAME)) return;

        event.getGuild().loadMembers().onSuccess(members -> {
            Member owner = null;
            for (Member m : members) {
                if (m.getEffectiveName().equals(textChannel.getName().substring(0, textChannel.getName().indexOf('-') - 1))) {
                    owner = m;
                    break;
                }
            }

            try {
                messageReaction.removeReaction(user).queue();
            } catch (Exception ignored) {
            }

            if (!emoji.equals(name) || (isOwnerControlled && !member.getId().equals(owner.getId())) || (!isOwnerControlled && member.getId().equals(owner.getId()))) return;

            setOwner(owner);
            setEvent(event);

            try {
                execute();
            } catch (Exception ignored) {
            }
        });
    }

    public abstract void execute() throws Exception;
}
