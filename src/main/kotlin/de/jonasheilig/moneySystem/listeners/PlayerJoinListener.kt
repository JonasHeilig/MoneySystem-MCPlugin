package de.jonasheilig.moneySystem.listeners

import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (!ConfigUtils.hasMoney(player.uniqueId)) {
            ConfigUtils.setMoney(player.uniqueId, ConfigUtils.getDefaultMoney())
        }
    }
}
