package dev.bukgeuk.polardiscord.bot

import dev.bukgeuk.polardiscord.ColoredChat
import dev.bukgeuk.polardiscord.PolarDiscord
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit

class onMessageReceived: ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (PolarDiscord.Error) return
        if (event.guild.id == PolarDiscord.serverID) {
            if (event.channel.id == PolarDiscord.commandChannelID) {
                val cmd = event.message.contentRaw.split(" ")
                if (cmd[0] == "/link") {
                    if (cmd.size >= 2) {
                        val data = PolarDiscord.load()
                        if (data[event.author.id] != null) {
                            event.channel.sendMessage("<@!${event.author.id}> 이미 연동이 되어있습니다").queue()
                            return
                        }
                        if (PolarDiscord.InviteTable[cmd[1]] == null) {
                            event.channel.sendMessage("<@!${event.author.id}> 올바르지 않은 코드입니다").queue()
                            return
                        }
                        val uuid = PolarDiscord.InviteTable[cmd[1]]!!
                        data[event.author.id] = uuid
                        PolarDiscord.InviteTable.remove(cmd[1])
                        PolarDiscord.save(data)

                        event.guild.addRoleToMember(event.author.id, event.guild.getRoleById(PolarDiscord.memberRoleID)!!).queue()

                        event.channel.sendMessage("<@!${event.author.id}> ${Bukkit.getOfflinePlayer(uuid).name}(으)로 연결됨").queue()
                        GlobalScope.launch {
                            val p = Bukkit.getPlayer(uuid)
                            for (x in 0 until 20) {
                                p?.spigot()?.sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${ChatColor.GREEN}${event.author.asTag}${ChatColor.BOLD}(으)로 연결됨"))
                                delay(500L)
                            }
                        }
                        return
                    }
                    event.channel.sendMessage("<@!${event.author.id}> 사용법: /link <code>").queue()
                    return
                }
            } else if (event.channel.id == PolarDiscord.chattingChannelID) {
                val data = PolarDiscord.load()
                val uuid = data[event.author.id] ?: return

                for (p in Bukkit.getOnlinePlayers()) {
                    p.sendMessage("<${Bukkit.getOfflinePlayer(uuid).name}> ${ColoredChat().hexToColor(event.message.contentRaw)}")
                }
            }
        }
    }
}