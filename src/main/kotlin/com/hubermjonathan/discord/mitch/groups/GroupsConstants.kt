package com.hubermjonathan.discord.mitch.groups

import net.dv8tion.jda.api.entities.Guild

private const val ARCHIVED_GROUPS_CATEGORY_ID = "1034570459670396959"
private const val PUBLIC_GROUPS_CATEGORY_ID = "1009404689806282772"
private const val SIGNUP_SHEET_TEXT_CHANNEL_ID = "1067692130232963142"

val Guild.archivedGroupsCategory
    get() = this.getCategoryById(ARCHIVED_GROUPS_CATEGORY_ID)!!

val Guild.publicGroupsCategory
    get() = this.getCategoryById(PUBLIC_GROUPS_CATEGORY_ID)!!

val Guild.signupSheetTextChannel
    get() = this.getTextChannelById(SIGNUP_SHEET_TEXT_CHANNEL_ID)!!
