package de.jonasheilig.moneySystem.commands

import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.Material

class AdminShopCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val page = if (args.isNotEmpty()) args[0].toIntOrNull() ?: 0 else 0
            openShop(sender, page)
        } else {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.")
        }
        return true
    }

    private fun openShop(player: Player, page: Int) {
        val shopItems = ConfigUtils.getShopItems().filter { it.type != Material.DIAMOND }
        val totalPages = (shopItems.size + 44) / 45
        val inventory = Bukkit.createInventory(null, 54, "AdminShop Seite $page von $totalPages")

        val startIndex = page * 45
        val endIndex = minOf(startIndex + 45, shopItems.size)

        for (i in startIndex until endIndex) {
            val item = shopItems[i].clone()
            val meta = item.itemMeta
            meta?.lore = listOf("Preis: ${ConfigUtils.getItemPrice(item)} Geld")
            item.itemMeta = meta
            inventory.addItem(item)
        }

        if (page > 0) {
            val prevPage = ItemStack(Material.ARROW)
            val meta = prevPage.itemMeta
            meta?.setDisplayName("Vorherige Seite")
            prevPage.itemMeta = meta
            inventory.setItem(45, prevPage)
        }

        if (endIndex < shopItems.size) {
            val nextPage = ItemStack(Material.ARROW)
            val meta = nextPage.itemMeta
            meta?.setDisplayName("Nächste Seite")
            nextPage.itemMeta = meta
            inventory.setItem(53, nextPage)
        }

        player.openInventory(inventory)
    }
}
