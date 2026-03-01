package com.techmc.koth

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class GameListener(private val plugin: KOTHPlugin) : Listener {

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        plugin.gameManager.removePlayer(event.player)
    }
}
