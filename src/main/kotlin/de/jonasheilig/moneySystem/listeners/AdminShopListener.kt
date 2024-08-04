package de.jonasheilig.moneySystem.listeners

import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack

class AdminShopListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val inventory = event.inventory

        if (inventory.type != InventoryType.CHEST) return

        val inventoryTitle = event.view.title

        if (inventoryTitle.startsWith("EditSellShop")) {
            event.isCancelled = true
            val clickedItem = event.currentItem ?: return

            if (clickedItem.type == Material.RED_STAINED_GLASS_PANE) {
                event.currentItem = ItemStack(Material.GREEN_STAINED_GLASS_PANE)
            } else if (clickedItem.type == Material.GREEN_STAINED_GLASS_PANE) {
                event.currentItem = ItemStack(Material.RED_STAINED_GLASS_PANE)
            } else {
                val price = ConfigUtils.getItemPrice(clickedItem)
                player.sendMessage("Das Item ${clickedItem.type} ist jetzt ${if (event.currentItem?.type == Material.GREEN_STAINED_GLASS_PANE) "zum Verkauf verfügbar" else "nicht zum Verkauf verfügbar"}.")
            }
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val inventoryTitle = event.view.title
        val inventory = event.inventory

        if (inventoryTitle.startsWith("EditSellShop")) {
            val sellItems = mutableListOf<ItemStack>()
            for (i in 0 until inventory.size) {
                val item = inventory.getItem(i) ?: continue
                if (inventory.getItem(i + 27)?.type == Material.GREEN_STAINED_GLASS_PANE) {
                    sellItems.add(item)
                }
            }
            ConfigUtils.setSellItems(sellItems)
        }
    }
}
