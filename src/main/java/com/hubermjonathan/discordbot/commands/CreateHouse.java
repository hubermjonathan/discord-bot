package com.hubermjonathan.discordbot.commands;

import com.hubermjonathan.discordbot.models.AdminCommand;
import com.hubermjonathan.discordbot.Constants;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.ChannelManager;

import java.util.EnumSet;

public class CreateHouse extends AdminCommand {
    public CreateHouse(String command) {
        super(command);
    }

    @Override
    public void execute() throws Exception {
        Guild guild = getEvent().getGuild();
        TextChannel oldTextChannel = null;

        for (Role role : guild.getRoles()) {
            if (role.isPublicRole() || role.isManaged()) {
                continue;
            }

            role.delete().queue();
        }
        for (Category category : guild.getCategories()) {
            category.delete().queue();
        }
        for (TextChannel textChannel : guild.getTextChannels()) {
            if (getArgs().length == 1 && textChannel.getId().equals(getArgs()[0])) {
                oldTextChannel = textChannel;
                oldTextChannel.getManager().reset(ChannelManager.PERMISSION).queue();
                continue;
            }

            textChannel.delete().queue();
        }
        for (VoiceChannel voiceChannel : guild.getVoiceChannels()) {
            voiceChannel.delete().queue();
        }

        guild.getPublicRole().getManager().revokePermissions(Permission.NICKNAME_CHANGE).queue();
        Role residentRole = guild.createRole()
                .setName(Constants.RESIDENT_ROLE_NAME)
                .setColor(Integer.parseInt("2ecc71", 16))
                .setHoisted(true)
                .complete();
        guild.createRole()
                .setName(Constants.FRIEND_ROLE_NAME)
                .setColor(Integer.parseInt("1abc9c", 16))
                .setHoisted(true)
                .queue();
        guild.createRole()
                .setName(Constants.VISITOR_ROLE_NAME)
                .setColor(Integer.parseInt("3498db", 16))
                .setHoisted(true)
                .queue();

        Category mainCategory = guild.createCategory(Constants.MAIN_CATEGORY_NAME).complete();
        if (oldTextChannel != null) {
            oldTextChannel.getManager()
                    .setParent(mainCategory)
                    .setName(Constants.MAIN_TEXT_CHANNEL_NAME)
                    .clearOverridesAdded()
                    .putPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.MESSAGE_READ))
                    .putPermissionOverride(residentRole, EnumSet.of(Permission.MESSAGE_READ), null)
                    .queue();
        } else {
            mainCategory.createTextChannel(Constants.MAIN_TEXT_CHANNEL_NAME)
                    .addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, EnumSet.of(Permission.MESSAGE_READ))
                    .addRolePermissionOverride(residentRole.getIdLong(), EnumSet.of(Permission.MESSAGE_READ), null)
                    .queue();
        }
        mainCategory.createTextChannel(Constants.BOT_CHANNEL_NAME)
                .queue();
        mainCategory.createVoiceChannel(Constants.MAIN_VOICE_CHANNEL_NAME).queue();
    }
}
