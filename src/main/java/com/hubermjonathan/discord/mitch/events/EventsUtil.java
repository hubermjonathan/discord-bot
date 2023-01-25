package com.hubermjonathan.discord.mitch.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import org.jetbrains.annotations.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class EventsUtil {
    public static MessageData buildEventMessage(
            @Nullable final Message message,
            final List<OptionMapping> options
    ) throws Exception {
        if (message != null) {
            final MessageEditBuilder messageBuilder = new MessageEditBuilder();

            messageBuilder.setEmbeds(updateEventEmbed(message.getEmbeds().get(0), options));
            messageBuilder.setComponents(
                    ActionRow.of(
                            Button.secondary("going", Emoji.fromUnicode("U+2705").getName()),
                            Button.secondary("notGoing", Emoji.fromUnicode("U+274C").getName()),
                            Button.secondary("maybe", Emoji.fromUnicode("U+2754").getName()),
                            Button.danger("delete", "delete")
                    )
            );

            return messageBuilder.build();
        } else {
            final MessageCreateBuilder messageBuilder = new MessageCreateBuilder();

            messageBuilder.setEmbeds(buildEventEmbed(options));
            messageBuilder.setComponents(
                    ActionRow.of(
                        Button.secondary("going", Emoji.fromUnicode("U+2705").getName()),
                        Button.secondary("notGoing", Emoji.fromUnicode("U+274C").getName()),
                        Button.secondary("maybe", Emoji.fromUnicode("U+2754").getName()),
                        Button.danger("delete", "delete")
                )
            );

            return messageBuilder.build();
        }
    }

    private static MessageEmbed buildEventEmbed(final List<OptionMapping> options) throws Exception {
        final EmbedBuilder embedBuilder = new EmbedBuilder();

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
            final MessageEmbed messageEmbed,
            final List<OptionMapping> options
    ) throws Exception {
        final EmbedBuilder embedBuilder = new EmbedBuilder(messageEmbed);

        if (getOption(options, "title") != null) {
            embedBuilder.setTitle(getOption(options, "title"));
        }

        if (getOption(options, "description") != null) {
            embedBuilder.setDescription(getOption(options, "description"));
        }

        embedBuilder.setFooter(
                getTimestamp(
                        messageEmbed.getFooter().getText(),
                        getOption(options, "date"),
                        getOption(options, "time")
                )
        );

        if (getOption(options, "picture") != null) {
            embedBuilder.setImage(getOption(options, "picture"));
        }

        return embedBuilder.build();
    }

    @Nullable
    private static String getOption(final List<OptionMapping> options, final String name) {
        for (final OptionMapping option : options) {
            if (option.getName().equals(name)) {
                return option.getAsString();
            }
        }

        return null;
    }

    private static String getTimestamp(
            @Nullable final String existingTimeStamp,
            @Nullable final String newDate,
            @Nullable final String newTime
    ) throws Exception {
        final LocalDate date = getDate(existingTimeStamp, newDate);
        final String formattedDate = date
                .format(DateTimeFormatter.ofPattern("E MMM dd"))
                .toLowerCase();
        final LocalTime time = getTime(existingTimeStamp, newTime);
        final String formattedTime = time
                .format(DateTimeFormatter.ofPattern("h:mma"))
                .toLowerCase();

        return String.format("%s @ %s", formattedDate, formattedTime);
    }

    public static LocalDate getDate(
            @Nullable final String existingTimestamp,
            @Nullable final String newDate
    ) throws Exception {
        final LocalDate today = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST")));
        final String date;

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
            final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("E MMM dd yyyy")
                    .toFormatter();

            return LocalDate.parse(String.format("%s %d", date, today.getYear()), dateTimeFormatter);
        } catch (final Exception ignored) {
        }

        try {
            final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("MMM dd yyyy")
                    .toFormatter();

            return LocalDate.parse(String.format("%s %d", date, today.getYear()), dateTimeFormatter);
        } catch (final Exception ignored) {
        }

        return today.with(TemporalAdjusters.next(DayOfWeek.valueOf(date.toUpperCase())));
    }

    public static LocalTime getTime(
            @Nullable final String existingTimestamp,
            @Nullable final String newTime
    ) throws Exception {
        final String time;

        if (newTime != null) {
            time = newTime;
        } else if (existingTimestamp != null) {
            time = existingTimestamp.substring(existingTimestamp.indexOf("@") + 2);
        } else {
            throw new Exception("need either existing timestamp or a new time");
        }

        try {
            final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("ha")
                    .toFormatter();

            return LocalTime.parse(time, dateTimeFormatter);
        } catch (final Exception ignored) {
        }

        final DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("h:mma")
                .toFormatter();

        return LocalTime.parse(time, dateTimeFormatter);
    }

    public static MessageEmbed addMemberToGoingField(final String member, final MessageEmbed messageEmbed) {
        final EmbedBuilder embedBuilder = new EmbedBuilder(messageEmbed);
        final List<MessageEmbed.Field> fields = removeMemberFromFields(member, embedBuilder.getFields());

        embedBuilder.clearFields();

        for (final MessageEmbed.Field field : fields) {
            if (field.getName().equals("going")) {
                embedBuilder.addField(addMemberToField(member, field));
            } else {
                embedBuilder.addField(removeMemberFromField(member, field));
            }
        }

        return embedBuilder.build();
    }

    public static MessageEmbed addMemberToNotGoingField(final String member, final MessageEmbed messageEmbed) {
        final EmbedBuilder embedBuilder = new EmbedBuilder(messageEmbed);
        final List<MessageEmbed.Field> fields = removeMemberFromFields(member, embedBuilder.getFields());

        embedBuilder.clearFields();

        for (final MessageEmbed.Field field : fields) {
            if (field.getName().equals("not going")) {
                embedBuilder.addField(addMemberToField(member, field));
            } else {
                embedBuilder.addField(removeMemberFromField(member, field));
            }
        }

        return embedBuilder.build();
    }

    public static MessageEmbed addMemberToMaybeField(final String member, final MessageEmbed messageEmbed) {
        final EmbedBuilder embedBuilder = new EmbedBuilder(messageEmbed);
        final List<MessageEmbed.Field> fields = removeMemberFromFields(member, embedBuilder.getFields());

        embedBuilder.clearFields();

        for (final MessageEmbed.Field field : fields) {
            if (field.getName().equals("maybe")) {
                embedBuilder.addField(addMemberToField(member, field));
            } else {
                embedBuilder.addField(removeMemberFromField(member, field));
            }
        }

        return embedBuilder.build();
    }

    private static MessageEmbed.Field addMemberToField(final String member, final MessageEmbed.Field field) {
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

    private static MessageEmbed.Field removeMemberFromField(final String member, final MessageEmbed.Field field) {
        StringBuilder stringBuilder = new StringBuilder(field.getValue());

        if (field.getValue().contains(member)) {
            stringBuilder.delete(
                    stringBuilder.indexOf(member) - 1,
                    stringBuilder.indexOf(member) + member.length() + 1
            );

            if (stringBuilder.toString().equals(">>>")) {
                stringBuilder = new StringBuilder(">>> -----");
            }
        }

        return new MessageEmbed.Field(field.getName(), stringBuilder.toString(), true);
    }

    private static List<MessageEmbed.Field> removeMemberFromFields(
            final String member, final List<MessageEmbed.Field> fields) {
        final List<MessageEmbed.Field> newFields = new ArrayList<>();

        for (final MessageEmbed.Field field : fields) {
            newFields.add(removeMemberFromField(member, field));
        }

        return newFields;
    }
}
