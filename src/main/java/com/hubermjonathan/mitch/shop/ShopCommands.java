package com.hubermjonathan.mitch.shop;

import com.hubermjonathan.mitch.Commands;
import com.hubermjonathan.mitch.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShopCommands extends Commands {
    Jedis jedis;

    public ShopCommands(MessageReceivedEvent event) {
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
                case ("rmute"):
                    buyRandomMute();
                    break;
                case ("mute"):
//                    buyMute();
                    break;
                case ("rename"):
//                    buyRename();
                    break;
                case ("shield"):
//                    buyShield();
                    break;
                default:
                    message.addReaction(Constants.DENY).queue();
            }
        } catch (ArrayIndexOutOfBoundsException | InterruptedException e) {
            sendShopMessage();
        }
    }

    private void sendShopMessage() {
        MessageChannel channel = getEvent().getChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("shop");
        embedBuilder.setDescription(
                "**\uD83C\uDFB2 random mute (350 dining dollars)** - `rmute` - server mute someone random for a minute\\n"
                        .concat("**\uD83D\uDD07 mute (700 dining dollars)** - `mute [person]` - server mute someone for a minute\\n")
                        .concat("**✏ rename (300 dining dollars)** - `rename [person] [name]` - rename someone\\n")
                        .concat("**\uD83D\uDEE1 mute shield (1000 dining dollars)** - `shield` - block mutes for 30 minutes\\n")
        );
        channel.sendMessage(embedBuilder.build()).queue();
    }

    private void buyRandomMute() throws InterruptedException {
        Map<String, String> oldMapping = jedis.hgetAll(getEvent().getAuthor().getId());
        double balance = Double.parseDouble(oldMapping.get("points"));
        long lastRandomMute = Long.parseLong(oldMapping.get("last_rmute"));
        if (balance < 350) {
            Message message = getEvent().getMessage();
            message.addReaction(Constants.NOT_ENOUGH_POINTS).queue();
            return;
        } else if ((System.currentTimeMillis() / 1000) - lastRandomMute < 60) {
            Message message = getEvent().getMessage();
            message.addReaction(Constants.ON_COOLDOWN).queue();
            return;
        }

        VoiceChannel voiceChannel = null;
        for (VoiceChannel vc : getEvent().getGuild().getVoiceChannels()) {
            if (vc.getMembers().contains(getEvent().getMember())) {
                voiceChannel = vc;
                if (vc.getMembers().size() < 3) {
                    Message message = getEvent().getMessage();
                    message.addReaction(Constants.DENY).queue();
                    return;
                }
            }
        }
        if (voiceChannel == null) {
            Message message = getEvent().getMessage();
            message.addReaction(Constants.NOT_IN_VC).queue();
            return;
        }

        List<Member> members = voiceChannel.getMembers();
        Member member = members.get(new Random().nextInt(members.size()));
        if (member.getVoiceState().isGuildMuted() || member.getRoles().contains(getEvent().getGuild().getRoleById(Constants.DEFAULT_ROLE_ID))) {
            Message message = getEvent().getMessage();
            message.addReaction(Constants.NOT_IN_VC).queue();
            return;
        }

        Map<String, String> newMapping = new HashMap<>();
        newMapping.put("points", String.valueOf(balance - 350));
        newMapping.put("last_rmute", String.valueOf(System.currentTimeMillis() / 1000));
        jedis.hmset(getEvent().getAuthor().getId(), newMapping);

        long lastShield = Long.parseLong(jedis.hget(member.getId(), "last_shield"));
        if ((System.currentTimeMillis() / 1000) - lastShield < 1800) {
            Message message = getEvent().getMessage();
            message.addReaction(Constants.SHIELD).queue();
            return;
        }

        Message message = getEvent().getMessage();
        message.addReaction(Constants.CONFIRM).queue();
        jedis.hset(member.getId(), "last_muted", String.valueOf(System.currentTimeMillis() / 1000));
        member.mute(true).queue();
        Thread.sleep(60 * 1000);
        member.mute(false).queue();
    }

//    private void buyMute() {
//
//    }
}
