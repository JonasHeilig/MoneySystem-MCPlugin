package de.jonasheilig.moneySystem.commands

import de.jonasheilig.moneySystem.MoneySystem
import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class ATMCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.")
            return true
        }

        val player = sender as Player
        openATMMenu(player)
        return true
    }

    private fun openATMMenu(player: Player) {
        val inventory = Bukkit.createInventory(null, 9, "ATM")

        val moneyItem = ItemStack(Material.GOLD_INGOT)
        val moneyMeta = moneyItem.itemMeta
        moneyMeta?.setDisplayName("Geld in Diamanten umtauschen")
        moneyItem.itemMeta = moneyMeta

        val diamondItem = ItemStack(Material.DIAMOND)
        val diamondMeta = diamondItem.itemMeta
        diamondMeta?.setDisplayName("Diamanten in Geld umtauschen")
        diamondItem.itemMeta = diamondMeta

        inventory.setItem(3, moneyItem)
        inventory.setItem(5, diamondItem)

        player.openInventory(inventory)
    }
}
