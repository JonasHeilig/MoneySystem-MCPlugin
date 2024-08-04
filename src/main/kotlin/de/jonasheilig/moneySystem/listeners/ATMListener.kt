package de.jonasheilig.moneySystem.listeners

import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

@Suppress("DEPRECATION")
class ATMListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.view.title != "ATM" || event.clickedInventory?.type != InventoryType.CHEST) return

        event.isCancelled = true
        val player = event.whoClicked
        val clickedItem = event.currentItem ?: return

        val exchangeRate = ConfigUtils.getExchangeRate()

        when (clickedItem.type) {
            Material.DIAMOND -> {
                // Diamanten in Geld umtauschen
                if (player.inventory.containsAtLeast(ItemStack(Material.DIAMOND), 1)) {
                    player.inventory.removeItem(ItemStack(Material.DIAMOND, 1))
                    ConfigUtils.setMoney(player.uniqueId, ConfigUtils.getMoney(player.uniqueId) + exchangeRate)
                    player.sendMessage("Du hast 1 Diamanten gegen $exchangeRate Geld getauscht.")
                } else {
                    player.sendMessage("Du hast nicht genug Diamanten, um Geld zu tauschen.")
                }
            }
            Material.GOLD_INGOT -> {
                // Geld in Diamanten umtauschen
                val money = ConfigUtils.getMoney(player.uniqueId)
                if (money >= exchangeRate) {
                    ConfigUtils.setMoney(player.uniqueId, money - exchangeRate)
                    player.inventory.addItem(ItemStack(Material.DIAMOND))
                    player.sendMessage("Du hast $exchangeRate Geld gegen 1 Diamanten getauscht.")
                } else {
                    player.sendMessage("Du hast nicht genug Geld, um einen Diamanten zu kaufen.")
                }
            }
            else -> {
            }
        }
    }
}
