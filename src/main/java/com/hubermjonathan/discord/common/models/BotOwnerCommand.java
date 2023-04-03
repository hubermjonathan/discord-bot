package com.hubermjonathan.discord.common.models;

import com.hubermjonathan.discord.common.Constants;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BotOwnerCommand extends Command {
    private final List<String> whitelistedChannels;

    public BotOwnerCommand(String name, CommandData commandData, @Nullable List<String> whitelistedChannels) {
        super(
                name,
                commandData.setDefaultPermissions(DefaultMemberPermissions.DISABLED),
                whitelistedChannels
        );

        this.whitelistedChannels = whitelistedChannels;
    }

    @Override
    protected boolean shouldIgnoreEvent() {
        return getEvent().getUser().isBot()
                || !getEvent().getName().equals(getName())
                || (System.getenv("DEV") == null && whitelistedChannels != null && !whitelistedChannels.contains(getEvent().getChannel().getId()))
                || (System.getenv("DEV") != null && !getEvent().getChannel().getId().equals(Constants.BOT_TESTING_CHANNEL_ID))
                || !getEvent().getUser().getId().equals(System.getenv("BOT_OWNER_ID"));
    }
}
