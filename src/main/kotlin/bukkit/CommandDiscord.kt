package dev.bukgeuk.polardiscord.bukkit

import dev.bukgeuk.polardiscord.PolarDiscord
import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CommandDiscord: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isNotEmpty()) {
            for (p in Bukkit.getOfflinePlayers()) {
                if (p.name == args[0]) {
                    val data = PolarDiscord.load()
                    for (id in data.keys) {
                        if (data[id] == p.uniqueId) {
                            val tag = PolarDiscord.bot.retrieveUserById(id).complete().asTag
                            sender.sendMessage("${ChatColor.of("#696969")}[PolarDiscord]${ChatColor.RESET} ${p.name} → $tag")
                            return true
                        }
                    }
                    sender.sendMessage("${ChatColor.of("#696969")}[PolarDiscord]${ChatColor.RESET} 해당 유저는 디스코드 연동이 되어있지 않습니다")
                    return true
                }
            }
            sender.sendMessage("${ChatColor.of("#696969")}[PolarDiscord]${ChatColor.RESET} 유저를 찾을 수 없습니다")
            return true
        }
        return false
    }
}