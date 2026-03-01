package com.techmc.koth

import org.bukkit.plugin.java.JavaPlugin

class KOTHPlugin : JavaPlugin() {

    lateinit var gameManager: GameManager
        private set

    override fun onEnable() {
        logger.info("KOTH Plugin enabled!")

        gameManager = GameManager(this)

        server.pluginManager.registerEvents(GameListener(this), this)
        getCommand("koth")?.setExecutor(KOTHCommand(this))
    }

    override fun onDisable() {
        logger.info("KOTH Plugin disabled!")
        gameManager.stopGame()
    }
}
