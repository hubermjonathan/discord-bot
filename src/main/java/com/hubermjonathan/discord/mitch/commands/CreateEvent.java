package com.hubermjonathan.discord.mitch.commands;

import com.hubermjonathan.discord.common.models.BotOwnerCommand;
import com.hubermjonathan.discord.mitch.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class CreateEvent extends BotOwnerCommand {
    public CreateEvent(String name, String description) {
        super(
                name,
                Commands.slash(name, description)
                        .addOption(OptionType.STRING, "title", "the title of the event", true)
                        .addOption(OptionType.STRING, "description", "the description of the event", true)
                        .addOption(OptionType.STRING, "date", "the date of the event", true)
                        .addOption(OptionType.STRING, "time", "the time of the event", true)
                        .addOption(OptionType.STRING, "picture", "the time of the event", false)
        );
    }

    private LocalDate getDate() {
        String date = getEvent().getOption("date").getAsString();
        LocalDate today = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST")));

        if (date.equals("today")) {
            return today;
        } else if (date.equals("tomorrow")) {
            return today.plusDays(1);
        }

        try {
            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("E MMM dd yyyy").toFormatter();
            return LocalDate.parse(String.format("%s %d", date, today.getYear()), dateTimeFormatter);
        } catch (Exception ignored) {
        }

        try {
            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("MMM dd yyyy").toFormatter();
            return LocalDate.parse(String.format("%s %d", date, today.getYear()), dateTimeFormatter);
        } catch (Exception ignored) {
        }

        return today.with(TemporalAdjusters.next(DayOfWeek.valueOf(date.toUpperCase())));
    }

    private LocalTime getTime() {
        String time = getEvent().getOption("time").getAsString();

        try {
            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("ha").toFormatter();
            return LocalTime.parse(time, dateTimeFormatter);
        } catch (Exception ignored) {
        }

        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("h:mma").toFormatter();
        return LocalTime.parse(time, dateTimeFormatter);
    }

    private String getTimestamp() {
        LocalDate date = getDate();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("E MMM dd")).toLowerCase();

        LocalTime time = getTime();
        String formattedTime = time.format(DateTimeFormatter.ofPattern("h:mma")).toLowerCase();

        return String.format("%s @ %s", formattedDate, formattedTime);
    }

    private MessageEmbed buildEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(getEvent().getOption("title").getAsString());
        embedBuilder.setDescription(getEvent().getOption("description").getAsString());
        embedBuilder.setColor(0xcfb991);
        embedBuilder.addField("going", ">>> -----", true);
        embedBuilder.addField("not going", ">>> -----", true);
        embedBuilder.addField("maybe", ">>> -----", true);
        embedBuilder.setFooter(getTimestamp());

        if (getEvent().getOption("picture") != null) {
            embedBuilder.setImage(getEvent().getOption("picture").getAsString());
        }

        return embedBuilder.build();
    }

    private Message buildMessage() {
        MessageBuilder messageBuilder = new MessageBuilder();

        messageBuilder.setEmbeds(buildEmbed());
        messageBuilder.setActionRows(ActionRow.of(
                Button.secondary("going", Emoji.fromUnicode(Constants.GOING).getName()),
                Button.secondary("notGoing", Emoji.fromUnicode(Constants.NOT_GOING).getName()),
                Button.secondary("maybe", Emoji.fromUnicode(Constants.MAYBE).getName()),
                Button.primary("edit", "edit"),
                Button.danger("delete", "delete")
        ));

        return messageBuilder.build();
    }

    private void scheduleReply(String messageId) {
        Timer timer = new Timer();
        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = ZoneId.of(ZoneId.SHORT_IDS.get("PST"));
        ZoneOffset zoneOffSet = zone.getRules().getOffset(now);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = getEvent().getChannel().asTextChannel().retrieveMessageById(messageId).complete();
                String eventTitle = message.getEmbeds().get(0).getTitle();
                ArrayList<String> membersGoing = new ArrayList<>(Arrays.asList(message.getEmbeds().get(0).getFields().get(0).getValue().replaceAll(">>> ", "").split("\n")));
                StringBuilder stringBuilder = new StringBuilder();

                for (String member : membersGoing) {
                    stringBuilder.append(getEvent().getGuild().getMembersByEffectiveName(member, true).get(0).getAsMention());
                    stringBuilder.append(" ");
                }

                message.reply(String.format("%s%s %s", stringBuilder, eventTitle, "is starting in 30 minutes!")).queue();
            }
        }, Date.from(getDate().atTime(getTime().minusMinutes(30)).toInstant(zoneOffSet)));
    }

    @Override
    public void execute() throws Exception {
        Message message = getEvent().getChannel().asTextChannel().sendMessage(buildMessage()).complete();

        getEvent().getChannel().asTextChannel().pinMessageById(message.getId()).queue();
        scheduleReply(message.getId());
    }
}
