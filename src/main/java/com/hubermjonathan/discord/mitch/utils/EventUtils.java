package com.hubermjonathan.discord.mitch.utils;

import com.hubermjonathan.discord.mitch.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import javax.annotation.Nullable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class EventUtils {
    private static LocalDate getDate(@Nullable String existingTimestamp, @Nullable String newDate) throws Exception {
        LocalDate today = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST")));
        String date;
        if (newDate != null) {
            date = newDate;
        } else if (existingTimestamp != null) {
            date = existingTimestamp.substring(0, existingTimestamp.indexOf("@") - 1);
        } else {
            throw new Exception("need either existing timestamp or a new date");
        }

        if (date.equals("today")) {
            return today;
        } else if (date.equals("tomorrow")) {
            return today.plusDays(1);
        }

        try {
            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive().appendPattern("E MMM dd yyyy").toFormatter();
            return LocalDate.parse(String.format("%s %d", date, today.getYear()), dateTimeFormatter);
        } catch (Exception ignored) {
        }

        try {
            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive().appendPattern("MMM dd yyyy").toFormatter();
            return LocalDate.parse(String.format("%s %d", date, today.getYear()), dateTimeFormatter);
        } catch (Exception ignored) {
        }

        return today.with(TemporalAdjusters.next(DayOfWeek.valueOf(date.toUpperCase())));
    }

    private static LocalTime getTime(@Nullable String existingTimestamp, @Nullable String newTime) throws Exception {
        String time;
        if (newTime != null) {
            time = newTime;
        } else if (existingTimestamp != null) {
            time = existingTimestamp.substring(existingTimestamp.indexOf("@") + 2);
        } else {
            throw new Exception("need either existing timestamp or a new time");
        }

        try {
            DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive().appendPattern("ha").toFormatter();
            return LocalTime.parse(time, dateTimeFormatter);
        } catch (Exception ignored) {
        }

        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive().appendPattern("h:mma").toFormatter();
        return LocalTime.parse(time, dateTimeFormatter);
    }

    private static String getTimestamp(
            @Nullable String existingTimeStamp, @Nullable String newDate, @Nullable String newTime) throws Exception {
        LocalDate date = getDate(existingTimeStamp, newDate);
        String formattedDate = date.format(DateTimeFormatter.ofPattern("E MMM dd")).toLowerCase();

        LocalTime time = getTime(existingTimeStamp, newTime);
        String formattedTime = time.format(DateTimeFormatter.ofPattern("h:mma")).toLowerCase();

        return String.format("%s @ %s", formattedDate, formattedTime);
    }

    @Nullable
    private static String getOption(List<OptionMapping> options, String name) {
        for (OptionMapping option : options) {
            if (option.getName().equals(name)) {
                return option.getAsString();
            }
        }

        return null;
    }

    private static MessageEmbed buildEventEmbed(List<OptionMapping> options) throws Exception {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle(getOption(options, "title"));
        embedBuilder.setDescription(getOption(options, "description"));
        embedBuilder.setColor(0xcfb991);
        embedBuilder.addField("going", ">>> -----", true);
        embedBuilder.addField("not going", ">>> -----", true);
        embedBuilder.addField("maybe", ">>> -----", true);
        embedBuilder.setFooter(getTimestamp(null, getOption(options, "date"), getOption(options, "time")));

        if (getOption(options, "picture") != null) {
            embedBuilder.setImage(getOption(options, "picture"));
        }

        return embedBuilder.build();
    }

    private static MessageEmbed updateEventEmbed(
            MessageEmbed messageEmbed, List<OptionMapping> options) throws Exception {
        EmbedBuilder embedBuilder = new EmbedBuilder(messageEmbed);

        if (getOption(options, "title") != null) {
            embedBuilder.setTitle(getOption(options, "title"));
        }

        if (getOption(options, "description") != null) {
            embedBuilder.setDescription(getOption(options, "description"));
        }

        embedBuilder.setFooter(getTimestamp(
                messageEmbed.getFooter().getText(), getOption(options, "date"), getOption(options, "time")));

        if (getOption(options, "picture") != null) {
            embedBuilder.setImage(getOption(options, "picture"));
        }

        return embedBuilder.build();
    }

    public static Message buildEventMessage(
            SlashCommandInteractionEvent event, @Nullable Message message) throws Exception {
        MessageBuilder messageBuilder = new MessageBuilder();

        if (message != null) {
            messageBuilder.setEmbeds(updateEventEmbed(message.getEmbeds().get(0), event.getOptions()));
        } else {
            messageBuilder.setEmbeds(buildEventEmbed(event.getOptions()));
        }

        messageBuilder.setActionRows(ActionRow.of(
                Button.secondary("going", Emoji.fromUnicode(Constants.GOING_EMOJI).getName()),
                Button.secondary("notGoing", Emoji.fromUnicode(Constants.NOT_GOING_EMOJI).getName()),
                Button.secondary("maybe", Emoji.fromUnicode(Constants.MAYBE_EMOJI).getName()),
                Button.danger("delete", "delete")
        ));

        return messageBuilder.build();
    }

    private static MessageEmbed.Field addMemberToField(String member, MessageEmbed.Field field) {
        StringBuilder stringBuilder = new StringBuilder(field.getValue());

        if (field.getValue().contains(member)) {
            return new MessageEmbed.Field(field.getName(), stringBuilder.toString(), true);
        }

        if (field.getValue().equals(">>> -----")) {
            stringBuilder = new StringBuilder(String.format(">>> %s", member));
        } else {
            stringBuilder.insert(stringBuilder.length(), String.format("\n%s", member));
        }

        return new MessageEmbed.Field(field.getName(), stringBuilder.toString(), true);
    }

    private static MessageEmbed.Field removeMemberFromField(String member, MessageEmbed.Field field) {
        StringBuilder stringBuilder = new StringBuilder(field.getValue());

        if (field.getValue().contains(member)) {
            stringBuilder.delete(stringBuilder.indexOf(member) - 1,
                    stringBuilder.indexOf(member) + member.length() + 1);

            if (stringBuilder.toString().equals(">>>")) {
                stringBuilder = new StringBuilder(">>> -----");
            }
        }

        return new MessageEmbed.Field(field.getName(), stringBuilder.toString(), true);
    }

    private static List<MessageEmbed.Field> removeMemberFromFields(String member, List<MessageEmbed.Field> fields) {
        List<MessageEmbed.Field> newFields = new ArrayList<>();

        for (MessageEmbed.Field field : fields) {
            newFields.add(removeMemberFromField(member, field));
        }

        return newFields;
    }

    public static MessageEmbed addMemberToGoingField(String member, MessageEmbed messageEmbed) {
        EmbedBuilder embedBuilder = new EmbedBuilder(messageEmbed);
        List<MessageEmbed.Field> fields = removeMemberFromFields(member, embedBuilder.getFields());

        embedBuilder.clearFields();

        for (MessageEmbed.Field field : fields) {
            if (field.getName().equals(Constants.GOING)) {
                embedBuilder.addField(addMemberToField(member, field));
            } else {
                embedBuilder.addField(removeMemberFromField(member, field));
            }
        }

        return embedBuilder.build();
    }

    public static MessageEmbed addMemberToNotGoingField(String member, MessageEmbed messageEmbed) {
        EmbedBuilder embedBuilder = new EmbedBuilder(messageEmbed);
        List<MessageEmbed.Field> fields = removeMemberFromFields(member, embedBuilder.getFields());

        embedBuilder.clearFields();

        for (MessageEmbed.Field field : fields) {
            if (field.getName().equals(Constants.NOT_GOING)) {
                embedBuilder.addField(addMemberToField(member, field));
            } else {
                embedBuilder.addField(removeMemberFromField(member, field));
            }
        }

        return embedBuilder.build();
    }

    public static MessageEmbed addMemberToMaybeField(String member, MessageEmbed messageEmbed) {
        EmbedBuilder embedBuilder = new EmbedBuilder(messageEmbed);
        List<MessageEmbed.Field> fields = removeMemberFromFields(member, embedBuilder.getFields());

        embedBuilder.clearFields();

        for (MessageEmbed.Field field : fields) {
            if (field.getName().equals(Constants.MAYBE)) {
                embedBuilder.addField(addMemberToField(member, field));
            } else {
                embedBuilder.addField(removeMemberFromField(member, field));
            }
        }

        return embedBuilder.build();
    }
}
