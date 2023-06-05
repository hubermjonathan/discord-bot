package com.hubermjonathan.discord.mitch

import com.hubermjonathan.discord.common.models.Command
import com.hubermjonathan.discord.mitch.management.ManagementFeature
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//region move me
val JDA.guild
    get() = this.getGuildById(Constants.SERVER_ID)!!

val JDA.botOwner
    get() = this.getUserById(System.getenv("BOT_OWNER_ID"))!!

val Guild.strangersRole
    get() = this.getRoleById(Constants.STRANGERS_ROLE_ID)
        ?: throw IllegalStateException("strangers role (${Constants.STRANGERS_ROLE_ID}) does not exist")

val Guild.friendsRole
    get() = this.getRoleById(Constants.FRIENDS_ROLE_ID)
        ?: throw IllegalStateException("friends role (${Constants.FRIENDS_ROLE_ID}) does not exist")
//endregion

class MitchBot : KoinComponent {
    private val jdaBuilder: JDABuilder by inject()
    private val jda: JDA by inject()
    private val managementFeature: ManagementFeature by inject()

    fun run() {
        val features = listOf(
            managementFeature,
        )

        features.forEach {
            it.load(jdaBuilder)
        }

        val commands = features.flatMap { it.commands }
        jda.guild.setCommands(commands).queue()

        features.forEach {
            it.startTasks()
        }
    }
}

private fun Guild.setCommands(commands: List<Command>): CommandListUpdateAction {
    return this.updateCommands().addCommands(commands.map { it.commandData })
}
