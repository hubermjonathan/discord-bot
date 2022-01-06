package com.hubermjonathan.discord.house.command;

import com.hubermjonathan.discord.house.Constants;
import com.hubermjonathan.discord.house.model.BotOwnerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.managers.ChannelManager;

import java.util.EnumSet;

public class BuildHouse extends BotOwnerCommand {
    public BuildHouse(String name, String description) {
        super(
                name,
                new CommandData(name, description).addOption(
                        OptionType.STRING, "id", "the id of the main chat channel to save", false
                )
        );
    }

    @Override
    public void execute() throws Exception {
        for (Role role : getEvent().getGuild().getRoles()) {
            if (!role.isPublicRole() && !role.isManaged()) {
                role.delete().queue();
            }
        }

        for (Category category : getEvent().getGuild().getCategories()) {
            category.delete().queue();
        }

        for (VoiceChannel voiceChannel : getEvent().getGuild().getVoiceChannels()) {
            voiceChannel.delete().queue();
        }

        TextChannel oldTextChannel = null;
        for (TextChannel textChannel : getEvent().getGuild().getTextChannels()) {
            if (getEvent().getOption("id") != null && textChannel.getId().equals(getEvent().getOption("id").getAsString())) {
                oldTextChannel = textChannel;
                oldTextChannel.getManager().reset(ChannelManager.PERMISSION).queue();
                continue;
            }

            textChannel.delete().queue();
        }

        getEvent().getGuild().getPublicRole().getManager()
                .revokePermissions(Permission.NICKNAME_CHANGE)
                .givePermissions(Permission.USE_SLASH_COMMANDS)
                .queue();
        Role residentRole = getEvent().getGuild().createRole()
                .setName(Constants.RESIDENT_ROLE_NAME)
                .setColor(Integer.parseInt(Constants.RESIDENT_ROLE_COLOR, 16))
                .setHoisted(true)
                .complete();
        getEvent().getGuild().createRole()
                .setName(Constants.FRIEND_ROLE_NAME)
                .setColor(Integer.parseInt(Constants.FRIEND_ROLE_COLOR, 16))
                .setHoisted(true)
                .queue();
        Role visitorRole = getEvent().getGuild().createRole()
                .setName(Constants.VISITOR_ROLE_NAME)
                .setColor(Integer.parseInt(Constants.VISITOR_ROLE_COLOR, 16))
                .setHoisted(true)
                .complete();

        for (Member member : getEvent().getGuild().getMembers()) {
            if (!member.getUser().isBot()) {
                getEvent().getGuild().addRoleToMember(member.getIdLong(), visitorRole).queue();
            }
        }

        Category mainCategory = getEvent().getGuild().createCategory(Constants.MAIN_CATEGORY_NAME).complete();
        if (oldTextChannel == null) {
            mainCategory.createTextChannel(Constants.MAIN_TEXT_CHANNEL_NAME)
                    .addRolePermissionOverride(getEvent().getGuild().getPublicRole().getIdLong(), null, EnumSet.of(Permission.MESSAGE_READ))
                    .addRolePermissionOverride(residentRole.getIdLong(), EnumSet.of(Permission.MESSAGE_READ), null)
                    .queue();
        } else {
            oldTextChannel.getManager()
                    .setParent(mainCategory)
                    .setName(Constants.MAIN_TEXT_CHANNEL_NAME)
                    .clearOverridesAdded()
                    .putPermissionOverride(getEvent().getGuild().getPublicRole(), null, EnumSet.of(Permission.MESSAGE_READ))
                    .putPermissionOverride(residentRole, EnumSet.of(Permission.MESSAGE_READ), null)
                    .queue();
        }
        mainCategory.createVoiceChannel(Constants.MAIN_VOICE_CHANNEL_NAME).queue();
    }
}
