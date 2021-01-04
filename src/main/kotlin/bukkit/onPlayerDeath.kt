package dev.bukgeuk.polardiscord.bukkit

import dev.bukgeuk.polardiscord.PolarDiscord
import net.dv8tion.jda.api.EmbedBuilder
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import java.awt.Color

class onPlayerDeath: Listener {
    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        if (e.entityType != EntityType.PLAYER) return
        val embed = EmbedBuilder()
        embed.setColor(Color.RED)
        embed.setAuthor("e.deathMessage", null, "https://crafatar.com/avatars/${e.entity.player?.uniqueId}")
        PolarDiscord.bot.getGuildById(PolarDiscord.serverID)?.getTextChannelById(PolarDiscord.eventChannelID)?.sendMessage(embed.build())?.queue()
    }
}