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
            val inventory = Bukkit.createInventory(null, 54, "EditAdminShop")
            val allItems = Material.values().filter { it.isItem }

            for (item in allItems.take(27)) {
                inventory.addItem(ItemStack(item))
            }

            for (i in 0..26) {
                inventory.setItem(i + 27, ItemStack(Material.RED_STAINED_GLASS_PANE))
            }

            sender.openInventory(inventory)
        } else {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.")
        }
        return true
    }
}
