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

        if (inventoryTitle.startsWith("EditAdminShop")) {
            event.isCancelled = true
            val clickedItem = event.currentItem ?: return

            if (clickedItem.type == Material.RED_STAINED_GLASS_PANE) {
                event.currentItem = ItemStack(Material.GREEN_STAINED_GLASS_PANE)
            } else if (clickedItem.type == Material.GREEN_STAINED_GLASS_PANE) {
                event.currentItem = ItemStack(Material.RED_STAINED_GLASS_PANE)
            } else {
                val price = ConfigUtils.getItemPrice(clickedItem)
                // Increase price by 10 (for example)
                ConfigUtils.setItemPrice(clickedItem, price + 10)
                player.sendMessage("Der Preis für ${clickedItem.type} ist jetzt ${price + 10} Geld.")
            }
        } else if (inventoryTitle.startsWith("AdminShop")) {
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
        } else {
            val parts = inventoryTitle.split(" ")
            if (parts.size >= 4) {
                val currentPage = parts[2].toIntOrNull() ?: return
                if (event.slot == 45 && event.currentItem?.type == Material.ARROW) {
                    player.closeInventory()
                    openEditShop(player, currentPage - 1)
                } else if (event.slot == 53 && event.currentItem?.type == Material.ARROW) {
                    player.closeInventory()
                    openEditShop(player, currentPage + 1)
                }
            }
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val inventoryTitle = event.view.title
        val inventory = event.inventory

        if (inventoryTitle.startsWith("EditAdminShop")) {
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

    private fun openEditShop(player: Player, page: Int) {
        val command = Bukkit.getPluginCommand("editadminshop") ?: return
        command.executor?.onCommand(player, command, "editadminshop", arrayOf(page.toString()))
    }
}
