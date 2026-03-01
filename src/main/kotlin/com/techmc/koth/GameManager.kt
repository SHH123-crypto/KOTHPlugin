package com.techmc.koth

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent

class GameManager(private val plugin: KOTHPlugin) {

    private var isGameActive = false
    private var zoneCenter: Location? = null
    private val zoneRadius = 10.0

    private val playerScores = mutableMapOf<Player, Int>()
    private var currentKing: Player? = null

    private var gameTask: BukkitRunnable? = null

    fun setZone(location: Location) {
        zoneCenter = location
        plugin.logger.info("Zone set at ${location.blockX}, ${location.blockY}, ${location.blockZ}")
    }

    fun startGame() {
        if (zoneCenter == null) {
            plugin.logger.warning("Cannot start game: zone not set!")
            return
        }

        if (isGameActive) {
            plugin.logger.warning("Game already active!")
            return
        }

        isGameActive = true
        playerScores.clear()
        currentKing = null

        Bukkit.broadcastMessage("§6[KOTH] Game started! Control the zone to earn points!")

        gameTask = object : BukkitRunnable() {
            override fun run() {
                if (!isGameActive) {
                    cancel()
                    return
                }
                updateGame()
            }
        }
        gameTask?.runTaskTimer(plugin, 0L, 20L)
    }

    fun stopGame() {
        if (!isGameActive) return

        isGameActive = false
        gameTask?.cancel()

        val winner = playerScores.maxByOrNull { it.value }
        if (winner != null) {
            Bukkit.broadcastMessage("§6[KOTH] Game over! Winner: ${winner.key.name} with ${winner.value} points!")
        } else {
            Bukkit.broadcastMessage("§6[KOTH] Game over! No winner.")
        }

        playerScores.clear()
        currentKing = null
    }

    fun removePlayer(player: Player) {
        playerScores.remove(player)
        if (currentKing == player) {
            currentKing = null
        }
    }

    private fun updateGame() {
        val zone = zoneCenter ?: return

        val playersInZone = Bukkit.getOnlinePlayers().filter { player ->
            player.world == zone.world && player.location.distance(zone) <= zoneRadius
        }

        when {
            playersInZone.isEmpty() -> {
                currentKing = null
            }
            playersInZone.size == 1 -> {
                val player = playersInZone.first()
                currentKing = player
                playerScores[player] = playerScores.getOrDefault(player, 0) + 1
                player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    TextComponent("§aControlling zone! Points: ${playerScores[player]}")
                )
            }
            else -> {
                currentKing = null
                playersInZone.forEach { player ->
                    player.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent("§cZone contested!")
                    )
                }
            }
        }
    }

    fun isInZone(player: Player): Boolean {
        val zone = zoneCenter ?: return false
        return player.world == zone.world && player.location.distance(zone) <= zoneRadius
    }

    fun isActive(): Boolean = isGameActive
}
