package de.jonasheilig.moneySystem.commands

import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MoneyCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val money = ConfigUtils.getMoney(sender.uniqueId)
            sender.sendMessage("Du hast $money Geld.")
        } else {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.")
        }
        return true
    }
}
