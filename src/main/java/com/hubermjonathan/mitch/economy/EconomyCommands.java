package com.hubermjonathan.mitch.economy;

import com.hubermjonathan.mitch.Commands;
import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EconomyCommands extends Commands {
    Jedis jedis;

    public EconomyCommands(MessageReceivedEvent event) {
        super(event);
        this.jedis = new Jedis(Constants.REDIS_URL);
    }

    @Override
    public void dispatch() {
        Message message = getEvent().getMessage();
        String content = message.getContentRaw();
        String[] tokens = content.split(" ");

        try {
            switch (tokens[1]) {
                case ("reset"):
                    resetEconomy();
                    break;
                case ("repair"):
                    repairEconomy();
                    break;
                case ("set"):
                    break;
                case ("balance"):
                case ("b"):
                    sendBalance();
                    break;
                case ("send"):
                    break;
                default:
                    message.addReaction(Constants.DENY).queue();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            message.addReaction(Constants.DENY).queue();
        }
    }

    private void resetEconomy() {
        jedis.flushDB();

        Role role = getEvent().getGuild().getRoleById(Constants.PROMOTED_ROLE_ID);
        List<Member> members = getEvent().getGuild().getMembersWithRoles(role);
        for (Member m : members) {
            Map<String, String> defaults = new HashMap<>();
            defaults.put("points", "0");
            defaults.put("total_points", "0");
            defaults.put("last_rmute", String.valueOf(System.currentTimeMillis() / 1000));
            defaults.put("last_mute", String.valueOf(System.currentTimeMillis() / 1000));
            defaults.put("last_shield", String.valueOf(System.currentTimeMillis() / 1000));
            defaults.put("last_muted", String.valueOf(System.currentTimeMillis() / 1000));
            jedis.hmset(m.getId(), defaults);
        }

        Message message = getEvent().getMessage();
        message.addReaction(Constants.CONFIRM).queue();
    }

    private void repairEconomy() {
        Role role = getEvent().getGuild().getRoleById(Constants.PROMOTED_ROLE_ID);
        List<Member> members = getEvent().getGuild().getMembersWithRoles(role);
        for (Member m : members) {
            Map<String, String> oldMapping = jedis.hgetAll(m.getId());
            Map<String, String> newMapping = new HashMap<>();

            if (!oldMapping.containsKey("points")) newMapping.put("points", "0");
            if (!oldMapping.containsKey("total_points")) newMapping.put("total_points", "0");
            if (!oldMapping.containsKey("last_rmute")) newMapping.put("last_rmute", String.valueOf(System.currentTimeMillis() / 1000));
            if (!oldMapping.containsKey("last_mute")) newMapping.put("last_mute", String.valueOf(System.currentTimeMillis() / 1000));
            if (!oldMapping.containsKey("last_shield")) newMapping.put("last_shield", String.valueOf(System.currentTimeMillis() / 1000));
            if (!oldMapping.containsKey("last_muted")) newMapping.put("last_muted", String.valueOf(System.currentTimeMillis() / 1000));

            if (!newMapping.isEmpty()) jedis.hmset(m.getId(), newMapping);
        }

        Message message = getEvent().getMessage();
        message.addReaction(Constants.CONFIRM).queue();
    }

    private void sendBalance() {
        double balance = Double.parseDouble(jedis.hget(getEvent().getAuthor().getId(), "points"));
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("balance");
        embedBuilder.setDescription(String.format("your current balance is **%.2f dining dollars** \uD83D\uDCB5", balance));
        getEvent().getAuthor().openPrivateChannel().complete().sendMessage(embedBuilder.build()).queue();

        Message message = getEvent().getMessage();
        message.addReaction(Constants.CONFIRM).queue();
    }
}
