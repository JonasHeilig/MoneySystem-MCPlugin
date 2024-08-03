package de.jonasheilig.moneySystem.commands

import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class MoneyToolCommand : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("moneysystem.moneytool")) {
            sender.sendMessage("Du hast keine Berechtigung, diesen Befehl zu verwenden.")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("Verwendung: /moneytool <get|add|remove> <Spieler> [Betrag]")
            return true
        }

        when (args[0].toLowerCase()) {
            "get" -> {
                if (args.size == 2) {
                    val target = Bukkit.getPlayer(args[1])
                    if (target != null) {
                        val money = ConfigUtils.getMoney(target.uniqueId)
                        sender.sendMessage("${target.name} hat $money Geld.")
                    } else {
                        sender.sendMessage("Spieler nicht gefunden.")
                    }
                } else {
                    sender.sendMessage("Verwendung: /moneytool get <Spieler>")
                }
            }

            "add" -> {
                if (args.size == 3) {
                    val target = Bukkit.getPlayer(args[1])
                    val amount = args[2].toIntOrNull()
                    if (target != null && amount != null) {
                        val newMoney = ConfigUtils.getMoney(target.uniqueId) + amount
                        ConfigUtils.setMoney(target.uniqueId, newMoney)
                        sender.sendMessage("${amount} Geld wurde zu ${target.name} hinzugefügt. Neuer Kontostand: $newMoney")
                    } else {
                        sender.sendMessage("Ungültiger Spielername oder Betrag.")
                    }
                } else {
                    sender.sendMessage("Verwendung: /moneytool add <Spieler> <Betrag>")
                }
            }

            "remove" -> {
                if (args.size == 3) {
                    val target = Bukkit.getPlayer(args[1])
                    val amount = args[2].toIntOrNull()
                    if (target != null && amount != null) {
                        val newMoney = ConfigUtils.getMoney(target.uniqueId) - amount
                        ConfigUtils.setMoney(target.uniqueId, newMoney)
                        sender.sendMessage("${amount} Geld wurde von ${target.name} entfernt. Neuer Kontostand: $newMoney")
                    } else {
                        sender.sendMessage("Ungültiger Spielername oder Betrag.")
                    }
                } else {
                    sender.sendMessage("Verwendung: /moneytool remove <Spieler> <Betrag>")
                }
            }

            else -> sender.sendMessage("Ungültiger Befehl. Verwendung: /moneytool <get|add|remove> <Spieler> [Betrag]")
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>? {
        return when (args.size) {
            1 -> listOf("get", "add", "remove").filter { it.startsWith(args[0], true) }
            2 -> Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[1], true) }
            else -> emptyList()
        }
    }
}
