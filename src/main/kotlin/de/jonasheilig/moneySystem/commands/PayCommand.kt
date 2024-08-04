package de.jonasheilig.moneySystem.commands

import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class PayCommand : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (args.size != 2) {
                sender.sendMessage("Usage: /pay <player> <amount>")
                return true
            }

            val targetName = args[0]
            val amount = args[1].toIntOrNull()

            if (amount == null || amount <= 0) {
                sender.sendMessage("Bitte geben Sie einen gültigen Betrag ein.")
                return true
            }

            val target = Bukkit.getPlayer(targetName)
            if (target == null) {
                sender.sendMessage("Spieler nicht gefunden.")
                return true
            }

            val senderUUID = sender.uniqueId
            val targetUUID = target.uniqueId

            if (!ConfigUtils.hasMoney(senderUUID) || ConfigUtils.getMoney(senderUUID) < amount) {
                sender.sendMessage("Du hast nicht genug Geld.")
                return true
            }

            ConfigUtils.setMoney(senderUUID, ConfigUtils.getMoney(senderUUID) - amount)
            ConfigUtils.setMoney(targetUUID, ConfigUtils.getMoney(targetUUID) + amount)

            sender.sendMessage("Du hast $amount Geld an ${target.name} gesendet.")
            target.sendMessage("Du hast $amount Geld von ${sender.name} erhalten.")

        } else {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.")
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        if (args.size == 1) {
            return Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[0], ignoreCase = true) }
        }
        return emptyList()
    }
}
