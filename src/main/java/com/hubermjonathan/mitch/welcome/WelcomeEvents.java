package com.hubermjonathan.mitch.welcome;


import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.List;

public class WelcomeEvents extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        if (event.getUser().isBot()) return;

        Guild guild = event.getGuild();
        List<Invite> invites = guild.retrieveInvites().complete();
        int youngestAge = Integer.MAX_VALUE;
        User inviter = null;
        for (Invite i : invites) {
            if (i.getMaxAge() < youngestAge) {
                youngestAge = i.getMaxAge();
                inviter = i.getInviter();
            }
        }

        String name;
        if (inviter == null) name = "???'s Friend";
        else if (inviter.getId().equals("703454392602329149")) name = "Jon's Friend";
        else if (inviter.getId().equals("196141424318611457")) name = "Jon's Friend";
        else if (inviter.getId().equals("269689252579770368")) name = "Andy's Friend";
        else if (inviter.getId().equals("135980703396397056")) name = "Collin's Friend";
        else if (inviter.getId().equals("204431167959728129")) name = "Daniel's Friend";
        else if (inviter.getId().equals("147848898772205569")) name = "Emanuel's Friend";
        else if (inviter.getId().equals("349934398067441665")) name = "Eric's Friend";
        else if (inviter.getId().equals("84113068073680896")) name = "Jay's Friend";
        else if (inviter.getId().equals("94844180680937472")) name = "Ray's Friend";
        else if (inviter.getId().equals("545837915251408898")) name = "Ray's Friend";
        else if (inviter.getId().equals("157673788585017344")) name = "Trevor's Friend";
        else if (inviter.getId().equals("200067699534069761")) name = "Aeson's Friend";
        else if (inviter.getId().equals("191025031134838785")) name = "Aneesh's Friend";
        else if (inviter.getId().equals("404462718209228810")) name = "Charlene's Friend";
        else if (inviter.getId().equals("318936907499438080")) name = "Davis' Friend";
        else if (inviter.getId().equals("98909400936247296")) name = "Drew's Friend";
        else if (inviter.getId().equals("329152118923460609")) name = "Jack's Friend";
        else if (inviter.getId().equals("404459939269181453")) name = "Jack's Friend";
        else if (inviter.getId().equals("330221855879200768")) name = "Julien's Friend";
        else if (inviter.getId().equals("374385728265912331")) name = "Neel's Friend";
        else if (inviter.getId().equals("235584239658205184")) name = "Sammie's Friend";
        else name = "???'s Friend";

        if (invites.size() > 1) name = name.concat("?");

        Member member = event.getMember();
        member.modifyNickname(name).queue();
        Role defaultRole = guild.getRoleById(System.getenv("DEFAULT_ROLE_ID"));
        guild.addRoleToMember(member, defaultRole).queue();
    }
}
