package com.techmc.koth

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class KOTHCommand(private val plugin: KOTHPlugin) : CommandExecutor {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("§cUsage: /koth <start|stop|setzone>")
            return true
        }

        when (args[0].lowercase()) {
            "start" -> {
                if (!sender.hasPermission("koth.admin")) {
                    sender.sendMessage("§cYou don't have permission!")
                    return true
                }
                plugin.gameManager.startGame()
                sender.sendMessage("§aGame started!")
            }

            "stop" -> {
                if (!sender.hasPermission("koth.admin")) {
                    sender.sendMessage("§cYou don't have permission!")
                    return true
                }
                plugin.gameManager.stopGame()
                sender.sendMessage("§aGame stopped!")
            }

            "setzone" -> {
                if (sender !is Player) {
                    sender.sendMessage("§cOnly players can set zones!")
                    return true
                }
                if (!sender.hasPermission("koth.admin")) {
                    sender.sendMessage("§cYou don't have permission!")
                    return true
                }
                plugin.gameManager.setZone(sender.location)
                sender.sendMessage("§aZone set at your location!")
            }

            else -> {
                sender.sendMessage("§cUsage: /koth <start|stop|setzone>")
            }
        }

        return true
    }
}
