package dev.bukgeuk.polardiscord.bot

import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.bukkit.Bukkit

class onReady: ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        Bukkit.getLogger().info("Started as ${event.jda.selfUser.asTag}")
    }
}