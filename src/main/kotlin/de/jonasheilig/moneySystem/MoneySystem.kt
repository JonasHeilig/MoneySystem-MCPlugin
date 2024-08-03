package de.jonasheilig.moneySystem

import de.jonasheilig.moneySystem.commands.*
import de.jonasheilig.moneySystem.listeners.*
import de.jonasheilig.moneySystem.utils.*
import org.bukkit.plugin.java.JavaPlugin

class MoneySystem : JavaPlugin() {

    companion object {
        lateinit var instance: MoneySystem
            private set
    }

    override fun onEnable() {

        saveDefaultConfig()
        ConfigUtils.setupMoneyFile(this)

        getCommand("money")?.setExecutor(MoneyCommand())
        getCommand("setmoney")?.apply {
            setExecutor(SetMoneyCommand())
            tabCompleter = SetMoneyCommand()
        }
        getCommand("moneytool")?.apply {
            setExecutor(MoneyToolCommand())
            tabCompleter = MoneyToolCommand()
        }
        server.pluginManager.registerEvents(PlayerJoinListener(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
