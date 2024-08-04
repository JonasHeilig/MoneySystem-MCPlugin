package de.jonasheilig.moneySystem.commands

import de.jonasheilig.moneySystem.utils.ConfigUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.min

class EditSellCommand : CommandExecutor, Listener {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (player.hasPermission("moneySystem.editsell")) {
                when (label) {
                    "editsetactive" -> {
                        if (args.size == 2) {
                            val itemName = args[0].uppercase()
                            val active = args[1].toBooleanStrictOrNull()
                            if (active != null) {
                                val itemMaterial = Material.matchMaterial(itemName)
                                if (itemMaterial != null) {
                                    ConfigUtils.setItemActive(itemMaterial, active)
                                    player.sendMessage("Der Verkauf von ${itemMaterial.name} wurde ${if (active) "aktiviert" else "deaktiviert"}.")
                                } else {
                                    player.sendMessage("Ungültiger Item-Name.")
                                }
                            } else {
                                player.sendMessage("Bitte gib 'true' oder 'false' an.")
                            }
                        } else {
                            player.sendMessage("Bitte gib den Item-Namen und den Status (true/false) an.")
                        }
                    }
                    "setdiscount" -> {
                        if (args.isNotEmpty()) {
                            val discount = args[0].toIntOrNull()
                            if (discount != null && discount in 0..100) {
                                ConfigUtils.setDiscount(discount)
                                player.sendMessage("Der Verkaufsabschlag wurde auf $discount% gesetzt.")
                            } else {
                                player.sendMessage("Bitte gib eine gültige Zahl zwischen 0 und 100 ein.")
                            }
                        } else {
                            player.sendMessage("Bitte gib einen Abschlag-Wert an.")
                        }
                    }
                    else -> {
                        player.sendMessage("Unbekannter Befehl.")
                    }
                }
            } else {
                player.sendMessage("Du hast keine Berechtigung, diesen Befehl auszuführen.")
            }
        } else {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.")
        }
        return true
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val inventory = event.inventory
        if (event.view.title.startsWith("EditSellShop")) {
            event.isCancelled = true
            when (event.slot) {
                45 -> if (event.view.title.contains("Seite")) openEditor(player, getCurrentPage(event.view.title) - 1)
                53 -> if (event.view.title.contains("Seite")) openEditor(player, getCurrentPage(event.view.title) + 1)
            }
        }
    }

    private fun openEditor(player: Player, page: Int) {
        val allItems = Material.values().filter { it.isItem }
        val totalPages = (allItems.size + 44) / 45
        val inventory = Bukkit.createInventory(null, 54, "EditSellShop Seite $page von $totalPages")

        val startIndex = page * 45
        val endIndex = min(startIndex + 45, allItems.size)

        for (i in startIndex until endIndex) {
            val item = ItemStack(allItems[i])
            val meta = item.itemMeta
            meta?.setDisplayName("Aktivieren/Deaktivieren")
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

        if (endIndex < allItems.size) {
            val nextPage = ItemStack(Material.ARROW)
            val meta = nextPage.itemMeta
            meta?.setDisplayName("Nächste Seite")
            nextPage.itemMeta = meta
            inventory.setItem(53, nextPage)
        }

        player.openInventory(inventory)
    }

    private fun getCurrentPage(title: String): Int {
        val regex = "Seite (\\d+) von".toRegex()
        val match = regex.find(title) ?: return 0
        return match.groupValues[1].toIntOrNull() ?: 0
    }
}
