package de.jonasheilig.moneySystem.commands

import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class SetMoneyCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player && args.size == 2) {
            if (!sender.hasPermission("moneysystem.setmoney")) {
                sender.sendMessage("Du hast keine Berechtigung, diesen Befehl zu verwenden.")
                return true
            }

            val target = Bukkit.getPlayer(args[0])
            val amount = args[1].toIntOrNull()

            if (target != null && amount != null) {
                ConfigUtils.setMoney(target.uniqueId, amount)
                sender.sendMessage("Du hast das Geld von ${target.name} auf $amount gesetzt.")
                target.sendMessage("Dein Geld wurde auf $amount gesetzt.")
            } else {
                sender.sendMessage("Ungültiger Spielername oder Betrag.")
            }
        } else {
            sender.sendMessage("Verwendung: /setmoney <Spieler> <Betrag>")
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>? {
        if (args.size == 1) {
            return Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[0], true) }
        }
        return emptyList()
    }
}
