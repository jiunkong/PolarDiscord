package dev.bukgeuk.polardiscord.bukkit

import dev.bukgeuk.polardiscord.PolarDiscord
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandLink: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val data = PolarDiscord.load()

            var id = ""
            for (item in data.keys) {
                if (data[item] == sender.uniqueId) {
                    id = item
                    break
                }
            }

            if (id == "") {
                val invite = PolarDiscord.createInvite(sender.uniqueId)
                if (invite == null) {
                    sender.sendMessage("${ChatColor.RED}An Error Occurred")
                    return true
                }

                sender.sendMessage("${ChatColor.of("#696969")}[PolarDiscord]${ChatColor.RESET} Code: ${invite.second}")
                val msg = TextComponent("${ChatColor.GREEN}${ChatColor.BOLD}                  [연동하기]")
                msg.clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, invite.first)

                sender.spigot().sendMessage(msg)
                sender.sendMessage("${ChatColor.of("#696969")}[PolarDiscord]${ChatColor.RESET} 초대가 ${PolarDiscord.inviteExpiration}분 후에 만료됩니다")
                return true
            }
            sender.sendMessage("${ChatColor.of("#696969")}[PolarDiscord]${ChatColor.RESET} 이미 디스코드와 연동되었습니다")
            return true
        }
        sender.sendMessage("${ChatColor.RED}${ChatColor.BOLD}Error: ${ChatColor.RESET}This command isn't available on the console")
        return true
    }
}