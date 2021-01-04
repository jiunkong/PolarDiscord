package dev.bukgeuk.polardiscord

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import dev.bukgeuk.polardiscord.bot.onGuildLeave
import dev.bukgeuk.polardiscord.bot.onMessageReceived
import dev.bukgeuk.polardiscord.bot.onReady
import dev.bukgeuk.polardiscord.bukkit.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import org.bukkit.Bukkit
import net.md_5.bungee.api.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

class PolarDiscord: JavaPlugin() {
    companion object {
        @JvmField var InviteTable: MutableMap<String, UUID> = mutableMapOf()
        lateinit var bot: JDA
        lateinit var serverID: String
        lateinit var chattingChannelID: String
        lateinit var inviteChannelID: String
        lateinit var eventChannelID: String
        lateinit var commandChannelID: String
        lateinit var memberRoleID: String
        lateinit var df: String
        var inviteExpiration = 10
        var Error = false

        fun createInvite(uuid: UUID): Pair<String, String>? { // Pair<url, rid>
            if (Error) return null
            val invite = bot.getGuildById(serverID)?.getGuildChannelById(inviteChannelID)?.createInvite()
            invite?.setMaxUses(1)
            invite?.setMaxAge(inviteExpiration * 60)
            invite?.setTemporary(true)
            val url = invite?.complete()?.url ?: return null

            var rid: String
            do {
                rid = RandomString().getRandomString(6)
            } while (InviteTable[rid] != null)

            InviteTable[rid] = uuid

            val timer = Timer()
            timer.schedule(object: TimerTask() {
                override fun run() {
                    if (InviteTable[rid] == null) return
                    Bukkit.getPlayer(uuid)?.sendMessage("${ChatColor.of("#696969")}[PolarDiscord]${ChatColor.RESET} 초대가 만료되었습니다")
                    InviteTable.remove(rid)
                }
            }, 1000 * 60 * inviteExpiration.toLong())

            return url to rid
        }

        fun load(): MutableMap<String, UUID> {
            val file = File("$df/data/DiscordToUUID.json")
            return if (file.exists()) jacksonObjectMapper().readValue(file) else mutableMapOf()
        }

        fun save(data: MutableMap<String, UUID>) {
            val file = File("$df/data/DiscordToUUID.json")
            jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValue(file, data)
        }
    }

    override fun onEnable() {
        logger.info("Plugin Enabled")

        config.addDefault("bot-token", "")
        config.addDefault("server-id", "")
        config.addDefault("chatting-channel-id", "")
        config.addDefault("invite-channel-id", "")
        config.addDefault("event-channel-id", "")
        config.addDefault("command-channel-id", "")
        config.addDefault("member-role-id", "")
        config.addDefault("invite-expiration", 10)
        config.options().copyDefaults(true)
        saveConfig()

        val file = File("${dataFolder.absolutePath}/data/")
        if (!file.exists()) file.mkdir()

        df = dataFolder.absolutePath

        getCommand("link")?.setExecutor(CommandLink())
        getCommand("discord")?.setExecutor(CommandDiscord())

        server.pluginManager.registerEvents(onPlayerJoin(), this)
        server.pluginManager.registerEvents(onPlayerQuit(), this)
        server.pluginManager.registerEvents(onPlayerDeath(), this)
        server.pluginManager.registerEvents(onPlayerChat(), this)

        botStart()
    }

    override fun onDisable() {
        logger.info("Plugin Disabled")

        bot.shutdownNow()
    }

    private fun botStart() {
        serverID = config.getString("server-id") ?: ""
        chattingChannelID = config.getString("chatting-channel-id") ?: ""
        inviteChannelID = config.getString("invite-channel-id") ?: ""
        eventChannelID = config.getString("event-channel-id") ?: ""
        commandChannelID = config.getString("command-channel-id") ?: ""
        memberRoleID = config.getString("member-role-id") ?: ""
        inviteExpiration = config.getInt("invite-expiration")
        val token = config.getString("bot-token")

        if (token == "" || serverID == "" || chattingChannelID == "" || inviteChannelID == "" || eventChannelID == "" || commandChannelID == "" || memberRoleID == "") {
            Error = true
            Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}[PolarDiscord] Discord Bot couldn't be started")
            Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}[PolarDiscord] Please edit the config file")
            return
        }

        bot = JDABuilder.createDefault(
            token,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_BANS,
            GatewayIntent.GUILD_INVITES
        ).build()
        bot.addEventListener(onReady())
        bot.addEventListener(onMessageReceived())
        bot.addEventListener(onGuildLeave())

        bot.presence.activity = Activity.playing("Polar Server")
    }
}