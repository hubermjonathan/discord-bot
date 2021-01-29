package com.hubermjonathan.mitch.commands;

import com.hubermjonathan.mitch.Command;
import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.util.stream.Collectors;

public class LeaveGroup extends Command {
    public LeaveGroup() {
        super("leave", "l");
    }

    @Override
    public void executeCommand() throws Exception {
        if (getArgs().length != 1) {
            getEvent().getMessage().addReaction(Constants.DENY).queue();
            throw new Exception();
        }

        Guild guild = getEvent().getGuild();
        List<String> groups = guild.getRoles().subList(7, guild.getRoles().size() - 1).stream().map(Role::getName).collect(Collectors.toList());
        String group = getArgs()[0];
        Message message = getEvent().getMessage();
        Member member = getEvent().getMember();
        Role role = guild.getRolesByName(group, true).get(0);

        if (!groups.contains(group) || !member.getRoles().contains(role)) {
            message.addReaction(Constants.DENY).queue();
            throw new Exception();
        }

        guild.removeRoleFromMember(member, role).queue();
    }
}
