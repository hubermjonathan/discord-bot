package com.hubermjonathan.discord.mitch.management

import net.dv8tion.jda.api.entities.Guild

private const val FRIENDS_ROLE_ID = "701161990919422002"
private const val STRANGERS_ROLE_ID = "701162007981981771"

val Guild.friendsRole
    get() = this.getRoleById(FRIENDS_ROLE_ID)!!

val Guild.strangersRole
    get() = this.getRoleById(STRANGERS_ROLE_ID)!!
