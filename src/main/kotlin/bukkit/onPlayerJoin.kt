package dev.bukgeuk.polardiscord.bukkit

import dev.bukgeuk.polardiscord.PolarDiscord
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.awt.Color


class onPlayerJoin: Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val embed = EmbedBuilder()
        embed.setColor(Color.GREEN)
        embed.setAuthor("${e.player.name} joined the game", null, "https://crafatar.com/avatars/${e.player.uniqueId}")
        PolarDiscord.bot.getGuildById(PolarDiscord.serverID)?.getTextChannelById(PolarDiscord.eventChannelID)?.sendMessage(embed.build())?.queue()

        val data = PolarDiscord.load()

        var id = ""
        for (item in data.keys) {
            if (data[item] == e.player.uniqueId) {
                id = item
                break
            }
        }

        if (id != "") {
            GlobalScope.launch {
                val tag = PolarDiscord.bot.retrieveUserById(id).complete().asTag
                for (x in 0 until 20) {
                    e.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${ChatColor.GREEN}${tag}${ChatColor.BOLD}(으)로 연결됨"))
                    delay(500L)
                }
            }
        } else {
            GlobalScope.launch {
                for (x in 0 until 20) {
                    e.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${ChatColor.RED}${ChatColor.BOLD}디스코드와 연동되지 않았습니다. /link 명령어로 디스코드 서버에 참가해주세요"))
                    delay (500L)
                }
            }
        }
    }
}