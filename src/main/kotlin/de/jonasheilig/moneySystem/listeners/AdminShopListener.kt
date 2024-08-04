package de.jonasheilig.moneySystem.listeners

import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.event.inventory.InventoryType

class AdminShopListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked
        val inventory = event.inventory

        if (inventory.type != InventoryType.CHEST) return

        val inventoryTitle = event.view.title

        if (inventoryTitle == "EditAdminShop") {
            event.isCancelled = true
            val clickedItem = event.currentItem ?: return

            if (clickedItem.type == Material.RED_STAINED_GLASS_PANE) {
                event.currentItem = ItemStack(Material.GREEN_STAINED_GLASS_PANE)
            } else if (clickedItem.type == Material.GREEN_STAINED_GLASS_PANE) {
                event.currentItem = ItemStack(Material.RED_STAINED_GLASS_PANE)
            } else {
                val price = ConfigUtils.getItemPrice(clickedItem)
                player.sendMessage("Der Preis für ${clickedItem.type} ist $price Geld.")
            }
        } else if (inventoryTitle == "AdminShop") {
            event.isCancelled = true
            val clickedItem = event.currentItem ?: return
            val price = ConfigUtils.getItemPrice(clickedItem)

            if (ConfigUtils.getMoney(player.uniqueId) >= price) {
                ConfigUtils.setMoney(player.uniqueId, ConfigUtils.getMoney(player.uniqueId) - price)
                player.inventory.addItem(clickedItem)
                player.sendMessage("${clickedItem.type} für $price Geld gekauft.")
            } else {
                player.sendMessage("Du hast nicht genug Geld, um ${clickedItem.type} zu kaufen.")
            }
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val inventoryTitle = event.view.title
        val inventory = event.inventory

        if (inventoryTitle == "EditAdminShop") {
            val shopItems = mutableListOf<ItemStack>()
            for (i in 0..26) {
                val item = inventory.getItem(i) ?: continue
                if (inventory.getItem(i + 27)?.type == Material.GREEN_STAINED_GLASS_PANE) {
                    shopItems.add(item)
                }
            }
            ConfigUtils.setShopItems(shopItems)
        }
    }
}
