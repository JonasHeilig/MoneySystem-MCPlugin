package de.jonasheilig.moneySystem.commands

import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class SellCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val player = sender
            val itemInHand = player.inventory.itemInMainHand

            if (itemInHand.type == Material.AIR) {
                player.sendMessage("Du hältst nichts in der Hand.")
                return true
            }

            val itemPrice = ConfigUtils.getItemPrice(itemInHand)
            val discount = ConfigUtils.getDiscount()
            val sellPrice = itemPrice * (100 - discount) / 100

            itemInHand.amount -= 1
            if (itemInHand.amount <= 0) {
                player.inventory.setItemInMainHand(ItemStack(Material.AIR))
            } else {
                player.inventory.setItemInMainHand(itemInHand)
            }

            val currentMoney = ConfigUtils.getMoney(player.uniqueId)
            ConfigUtils.setMoney(player.uniqueId, currentMoney + sellPrice)

            player.sendMessage("Du hast ${itemInHand.type} für $sellPrice Geld verkauft.")
        } else {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.")
        }
        return true
    }
}
