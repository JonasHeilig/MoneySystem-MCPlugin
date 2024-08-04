package de.jonasheilig.moneySystem.utils

import de.jonasheilig.moneySystem.MoneySystem
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.Material
import java.io.File
import java.util.UUID

object ConfigUtils {
    private lateinit var moneyFile: File
    private lateinit var moneyConfig: YamlConfiguration
    private lateinit var shopFile: File
    private lateinit var shopConfig: YamlConfiguration

    fun setupMoneyFile(plugin: MoneySystem) {
        moneyFile = File(plugin.dataFolder, "money.yml")
        if (!moneyFile.exists()) {
            moneyFile.createNewFile()
        }
        moneyConfig = YamlConfiguration.loadConfiguration(moneyFile)
    }

    fun setupShopFile(plugin: MoneySystem) {
        shopFile = File(plugin.dataFolder, "shop.yml")
        if (!shopFile.exists()) {
            shopFile.createNewFile()
        }
        shopConfig = YamlConfiguration.loadConfiguration(shopFile)
    }

    fun getMoney(uuid: UUID): Int {
        return moneyConfig.getInt(uuid.toString(), getDefaultMoney())
    }

    fun setMoney(uuid: UUID, amount: Int) {
        moneyConfig.set(uuid.toString(), amount)
        saveMoneyFile()
    }

    fun getItemPrice(item: ItemStack): Int {
        return shopConfig.getInt("${item.type}.price", 100)
    }

    fun getShopItems(): List<ItemStack> {
        val items = mutableListOf<ItemStack>()
        for (key in shopConfig.getConfigurationSection("").getKeys(false)) {
            val material = Material.getMaterial(key) ?: continue
            val price = shopConfig.getInt("$key.price", 100)
            val item = ItemStack(material)
            val meta = item.itemMeta
            meta.setDisplayName("$price Geld")
            item.itemMeta = meta
            items.add(item)
        }
        return items
    }

    fun setShopItems(items: List<ItemStack>) {
        shopConfig.getKeys(false).forEach { shopConfig.set(it, null) }
        for (item in items) {
            shopConfig.set("${item.type}.price", getItemPrice(item))
        }
        saveShopFile()
    }

    fun getDefaultMoney(): Int {
        return MoneySystem.instance.config.getInt("default-money", 100)
    }

    private fun saveMoneyFile() {
        moneyConfig.save(moneyFile)
    }

    private fun saveShopFile() {
        shopConfig.save(shopFile)
    }
}
