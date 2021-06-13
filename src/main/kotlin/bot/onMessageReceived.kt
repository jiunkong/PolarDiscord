package dev.bukgeuk.polardiscord.bot

import dev.bukgeuk.polardiscord.ColoredChat
import dev.bukgeuk.polardiscord.PolarDiscord
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import java.lang.management.ManagementFactory
import com.sun.management.OperatingSystemMXBean
import org.bukkit.GameRule
import java.awt.Color
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class onMessageReceived: ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (PolarDiscord.Error) return
        if (event.guild.id == PolarDiscord.serverID) {
            if (event.channel.id == PolarDiscord.commandChannelID) {
                val cmd = event.message.contentRaw.split(" ")
                when (cmd[0]) {
                    "/link" -> {
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
                    "/status" -> {
                        val osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean::class.java)
                        val embed = EmbedBuilder()
                        embed.setTitle("서버 사용량")
                        val cpu = (osBean.systemCpuLoad * 100).roundToInt()
                        val cpuSymbol = if (cpu > 90) "\uD83D\uDCD5" else if (cpu > 70) "\uD83D\uDCD9" else "\uD83D\uDCD7"
                        embed.addField("$cpuSymbol CPU 사용량", "**$cpu%**", false)
                        val memory = (100 - (((osBean.freePhysicalMemorySize.toDouble()/1024/1024/1024) / (osBean.totalPhysicalMemorySize.toDouble()/1024/1024/1024)) * 100)).roundToLong()
                        val memorySymbol = if (memory > 90) "\uD83D\uDCD5" else if (memory > 70) "\uD83D\uDCD9" else "\uD83D\uDCD7"
                        embed.addField("$memorySymbol 메모리 사용량", "**$memory%**", false)
                        val value = (cpu + memory) / 2
                        embed.setColor(if (value > 90) Color.RED else if (value > 70) Color.ORANGE else Color.GREEN)

                        event.channel.sendMessage(embed.build()).queue()
                    }
                    "/info" -> {
                        val difficulty: MutableList<String> = mutableListOf()
                        val keepInventory: MutableList<String> = mutableListOf()
                        for (world in Bukkit.getWorlds()) {
                            difficulty.add("${world.name}: ${world.difficulty}")
                            keepInventory.add("${world.name}: ${if (world.getGameRuleValue(GameRule.KEEP_INVENTORY) == true) "켜짐" else "꺼짐"}")
                        }

                        val embed = EmbedBuilder()
                        embed.setTitle("서버 정보")
                        embed.setColor(Color(0x6495ED))
                        embed.addField("버전", "**${Bukkit.getVersion()}**", false)
                        embed.addField("난이도", difficulty.joinToString(", "), false)
                        embed.addField("인벤세이브", keepInventory.joinToString(", "), false)

                        event.channel.sendMessage(embed.build()).queue()
                    }
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