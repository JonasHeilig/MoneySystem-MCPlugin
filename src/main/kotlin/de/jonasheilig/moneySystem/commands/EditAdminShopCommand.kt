package de.jonasheilig.moneySystem.commands

import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class EditAdminShopCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val page = if (args.isNotEmpty()) args[0].toIntOrNull() ?: 0 else 0
            openEditor(sender, page)
        } else {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.")
        }
        return true
    }

    private fun openEditor(player: Player, page: Int) {
        val allItems = Material.values().filter { it.isItem }
        val totalPages = (allItems.size + 44) / 45
        val inventory = Bukkit.createInventory(null, 54, "EditAdminShop Seite $page von $totalPages")

        val startIndex = page * 45
        val endIndex = minOf(startIndex + 45, allItems.size)

        for (i in startIndex until endIndex) {
            val item = ItemStack(allItems[i])
            inventory.addItem(item)
        }

        for (i in 0 until (endIndex - startIndex)) {
            inventory.setItem(i + 27, ItemStack(Material.RED_STAINED_GLASS_PANE))
        }

        // Navigation buttons
        if (page > 0) {
            val prevPage = ItemStack(Material.ARROW)
            val meta = prevPage.itemMeta
            meta?.setDisplayName("Vorherige Seite")
            prevPage.itemMeta = meta
            inventory.setItem(45, prevPage)
        }

        if (endIndex < allItems.size) {
            val nextPage = ItemStack(Material.ARROW)
            val meta = nextPage.itemMeta
            meta?.setDisplayName("Nächste Seite")
            nextPage.itemMeta = meta
            inventory.setItem(53, nextPage)
        }

        player.openInventory(inventory)
    }
}
