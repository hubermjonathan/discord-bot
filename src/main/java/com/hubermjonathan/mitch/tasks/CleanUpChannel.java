package com.hubermjonathan.mitch.tasks;

import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.TimerTask;

public class CleanUpChannel extends TimerTask {
    private final JDA jda;

    public CleanUpChannel(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void run() {
        TextChannel channel = jda.getTextChannelById(Constants.BOT_CHANNEL_ID);
        LinkedList<Message> messages = new LinkedList<>(channel.getHistoryFromBeginning(100).complete().getRetrievedHistory());

        messages.remove(messages.size() - 1);
        messages.removeIf(message -> message.getTimeCreated().isAfter(OffsetDateTime.now().minusSeconds(10)));
        channel.purgeMessages(messages);
    }
}
