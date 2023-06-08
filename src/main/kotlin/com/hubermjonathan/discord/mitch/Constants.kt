package com.hubermjonathan.discord.mitch

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild

private const val BOT_OWNER_ID = "196141424318611457"
private const val BOT_TESTING_CHANNEL_ID = "1065540592731435018"
private const val PURDUDES_SERVER_ID = "378759761073668096"

val JDA.botOwner
    get() = this.getUserById(BOT_OWNER_ID)!!

val Guild.botTestingChannel
    get() = this.getTextChannelById(BOT_TESTING_CHANNEL_ID)!!

val JDA.purdudesGuild
    get() = this.getGuildById(PURDUDES_SERVER_ID)!!
