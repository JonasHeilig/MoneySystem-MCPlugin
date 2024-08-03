package de.jonasheilig.moneySystem.utils

import de.jonasheilig.moneySystem.MoneySystem
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.UUID

object ConfigUtils {
    private lateinit var moneyFile: File
    private lateinit var moneyConfig: YamlConfiguration

    fun setupMoneyFile(plugin: MoneySystem) {
        moneyFile = File(plugin.dataFolder, "money.yml")
        if (!moneyFile.exists()) {
            moneyFile.createNewFile()
        }
        moneyConfig = YamlConfiguration.loadConfiguration(moneyFile)
    }

    fun getMoney(uuid: UUID): Int {
        return moneyConfig.getInt(uuid.toString(), getDefaultMoney())
    }

    fun setMoney(uuid: UUID, amount: Int) {
        moneyConfig.set(uuid.toString(), amount)
        saveMoneyFile()
    }

    fun hasMoney(uuid: UUID): Boolean {
        return moneyConfig.contains(uuid.toString())
    }

    fun getDefaultMoney(): Int {
        return MoneySystem.getPlugin(MoneySystem::class.java).config.getInt("default-money", 100)
    }

    private fun saveMoneyFile() {
        moneyConfig.save(moneyFile)
    }
}
