package de.jonasheilig.moneySystem.utils

import de.jonasheilig.moneySystem.MoneySystem
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
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

    fun hasMoney(uuid: UUID): Boolean {
        return moneyConfig.contains(uuid.toString())
    }

    fun getItemPrice(item: ItemStack): Int {
        val basePrice = shopConfig.getInt("${item.type}.price", 100)
        val discount = getDiscount()
        return (basePrice * (100 - discount) / 100).toInt()
    }

    fun setItemPrice(item: ItemStack, price: Int) {
        shopConfig.set("${item.type}.price", price)
        saveShopFile()
    }

    fun setItemActive(material: Material, active: Boolean) {
        shopConfig.set("${material.name}.active", active)
        saveShopFile()
    }

    fun isItemActive(material: Material): Boolean {
        return shopConfig.getBoolean("${material.name}.active", true)
    }

    fun getShopItems(): List<ItemStack> {
        val items = mutableListOf<ItemStack>()
        for (key in shopConfig.getConfigurationSection("")?.getKeys(false) ?: emptySet()) {
            val material = Material.getMaterial(key) ?: continue
            if (!isItemActive(material)) continue
            val price = getItemPrice(ItemStack(material))
            val item = ItemStack(material)
            val meta = item.itemMeta
            meta?.setDisplayName(material.name)
            meta?.lore = listOf("Preis: $price Geld")
            item.itemMeta = meta
            items.add(item)
        }
        return items
    }

    fun setSellItems(items: List<ItemStack>) {
        shopConfig.getKeys(false).forEach { key -> shopConfig.set(key, null) }

        for (item in items) {
            if (item.type == Material.GREEN_STAINED_GLASS_PANE) continue
            setItemPrice(item, getItemPrice(item))
            setItemActive(item.type, true)
        }
        saveShopFile()
    }

    fun getDiscount(): Int {
        return MoneySystem.instance.config.getInt("discount", 0)
    }

    fun setDiscount(discount: Int) {
        MoneySystem.instance.config.set("discount", discount)
        MoneySystem.instance.saveConfig()
    }

    private fun saveMoneyFile() {
        moneyConfig.save(moneyFile)
    }

    private fun saveShopFile() {
        shopConfig.save(shopFile)
    }

    private fun getDefaultMoney(): Int {
        return 1000 // Beispielwert
    }
}
