package dev.bukgeuk.polardiscord.bukkit

import dev.bukgeuk.polardiscord.PolarDiscord
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class onPlayerChat: Listener {
    @EventHandler
    fun onPlayerChat(e: AsyncPlayerChatEvent) {
        PolarDiscord.bot.getGuildById(PolarDiscord.serverID)?.getTextChannelById(PolarDiscord.chattingChannelID)?.sendMessage("<${e.player.name}> ${e.message}")?.queue()
    }
}