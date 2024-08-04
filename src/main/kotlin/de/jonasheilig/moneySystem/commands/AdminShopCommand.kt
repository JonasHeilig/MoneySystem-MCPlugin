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
import org.bukkit.inventory.meta.ItemMeta

class AdminShopCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val inventory = Bukkit.createInventory(null, 54, "AdminShop")
            val shopItems = ConfigUtils.getShopItems()

            for (item in shopItems) {
                inventory.addItem(item)
            }

            sender.openInventory(inventory)
        } else {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.")
        }
        return true
    }
}
