package dev.bukgeuk.polardiscord

import net.md_5.bungee.api.ChatColor

class RandomString {
    private val sequence = "ABCDEF1234567890"

    fun getRandomString(length: Int): String {
        var str = ""
        for (i in 0 until length) str += sequence.random()

        return str
    }
}

class ColoredChat {
    private val pattern = Regex("""\$\{(#[a-zA-Z0-9]{6})}""")

    fun hexToColor(text: String): String {
        var str = text
        var res = pattern.find(str)
        while (res != null) {
            str = str.replace(res.value, "${ChatColor.of(res.groupValues[1])}")
            res = pattern.find(str)
        }

        return ChatColor.translateAlternateColorCodes('&', str)
    }
}