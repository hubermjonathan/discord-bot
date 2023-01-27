package com.hubermjonathan.discord.mitch.draft.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import com.hubermjonathan.discord.mitch.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartDraft extends BotOwnerCommand {
    public StartDraft(String name, String description) {
        super(
                name,
                Commands
                        .slash(name, description)
                        .addOption(OptionType.INTEGER, "rounds", "the number of rounds in the draft", true)
                        .addOption(OptionType.STRING, "channel", "the id of the channel to draft from", true)
                        .setDefaultPermissions(DefaultMemberPermissions.DISABLED),
                Constants.BOT_OWNER_ID
        );
    }

    @Override
    public void execute() throws Exception {
        TextChannel channel = getEvent()
                .getGuild()
                .getTextChannelById(getEvent().getOption("channel").getAsString());
        List<String> draftedMembers = draftMembers(channel.getMembers(), getEvent().getOption("rounds").getAsInt());

        channel
                .sendMessage(buildMessage(draftedMembers))
                .queue();
    }

    private MessageCreateData buildMessage(List<String> draftedMembers) {
        MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("\uD83D\uDEA8 HANG OUT DRAFT \uD83D\uDEA8");
        embedBuilder.setDescription("THE FOLLOWING PEOPLE HAVE BEEN SELECTED TO PARTICIPATE IN MANDATORY FUN TIME");
        embedBuilder.setColor(0xcfb991);
        embedBuilder.addField("DRAFTED", String.join("\n", draftedMembers), true);
        messageBuilder.setEmbeds(embedBuilder.build());

        return messageBuilder.build();
    }

    private List<String> draftMembers(List<Member> members, int numberOfRounds) {
        Random r = new Random();
        List<Member> m = new ArrayList<>(members);
        List<String> l = new ArrayList<>();

        for (int i = 0; i < numberOfRounds; i++) {
            int randomIndex = r.nextInt(m.size());

            l.add(m.get(randomIndex).getEffectiveName());
            m.remove(randomIndex);
        }

        return l;
    }
}
