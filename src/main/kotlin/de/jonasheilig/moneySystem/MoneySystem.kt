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
        instance = this
        saveDefaultConfig()
        ConfigUtils.setupMoneyFile(this)
        ConfigUtils.setupShopFile(this)

        getCommand("money")?.setExecutor(MoneyCommand())
        getCommand("setmoney")?.apply {
            setExecutor(SetMoneyCommand())
            tabCompleter = SetMoneyCommand()
        }

        getCommand("moneytool")?.apply {
            setExecutor(MoneyToolCommand())
            tabCompleter = MoneyToolCommand()
        }

        getCommand("pay")?.apply {
            setExecutor(PayCommand())
            tabCompleter = PayCommand()
        }

        getCommand("adminshop")?.setExecutor(AdminShopCommand())
        getCommand("editadminshop")?.setExecutor(EditAdminShopCommand())
        getCommand("atm")?.setExecutor(ATMCommand())
        getCommand("sell")?.setExecutor(SellCommand())
        getCommand("editsell")?.setExecutor(EditSellCommand())

        server.pluginManager.registerEvents(PlayerJoinListener(), this)
        server.pluginManager.registerEvents(AdminShopListener(), this)
        server.pluginManager.registerEvents(ATMListener(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
