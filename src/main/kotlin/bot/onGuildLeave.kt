package dev.bukgeuk.polardiscord.bot

import dev.bukgeuk.polardiscord.PolarDiscord
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit

class onGuildLeave: ListenerAdapter() {
    override fun onGuildMemberRemove(event: GuildMemberRemoveEvent) {
        val data = PolarDiscord.load()
        val uuid = data[event.user.id]
        if (uuid != null) {
            data.remove(event.user.id)
            PolarDiscord.save(data)
            GlobalScope.launch {
                val p = Bukkit.getPlayer(uuid)
                for (x in 0 until 20) {
                    p?.spigot()?.sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${ChatColor.RED}${ChatColor.BOLD}디스코드와 연동되지 않았습니다. /link 명령어로 디스코드 서버에 참가해주세요"))
                    delay (500L)
                }
            }
        }
    }
}