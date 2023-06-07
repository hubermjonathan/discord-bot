package com.hubermjonathan.discord.mitch

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild

val Guild.friendsRole
    get() = this.getRoleById(Constants.FRIENDS_ROLE_ID)!!

val Guild.strangersRole
    get() = this.getRoleById(Constants.STRANGERS_ROLE_ID)!!

val JDA.botOwner
    get() = this.getUserById(Constants.BOT_OWNER_ID)!!

val JDA.guild
    get() = this.getGuildById(Constants.SERVER_ID)!!
