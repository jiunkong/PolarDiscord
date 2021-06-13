package dev.bukgeuk.polardiscord.bukkit

import dev.bukgeuk.polardiscord.PolarDiscord
import net.dv8tion.jda.api.EmbedBuilder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.awt.Color

class onPlayerQuit: Listener {
    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        val embed = EmbedBuilder()
        embed.setColor(Color.RED)
        embed.setAuthor("${e.player.name} left the game", null, "https://crafatar.com/avatars/${e.player.uniqueId}")
        PolarDiscord.bot.getGuildById(PolarDiscord.serverID)?.getTextChannelById(PolarDiscord.eventChannelID)?.sendMessage(embed.build())?.queue()
    }
}