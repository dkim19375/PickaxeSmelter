/*
 *     PickaxeSmelter, a plugin that automatically smelts items mined
 *     Copyright (C) 2021  dkim19375
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.dkim19375.pickaxesmelter.command

import me.dkim19375.dkimbukkitcore.function.formatAll
import me.dkim19375.dkimbukkitcore.function.toPlayer
import me.dkim19375.pickaxesmelter.PickaxeSmelter
import me.dkim19375.pickaxesmelter.enumclass.ErrorType
import me.dkim19375.pickaxesmelter.enumclass.Permissions
import me.dkim19375.pickaxesmelter.extension.*
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class PickaxeSmelterCmd(private val plugin: PickaxeSmelter) : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>,
    ): Boolean {
        if (args.isEmpty()) {
            sender.sendHelpMessage(label)
            return true
        }
        when (args[0].lowercase()) {
            "help" -> {
                sender.sendHelpMessage(label)
                return true
            }
            "reload" -> {
                if (!sender.hasPermission(Permissions.RELOAD)) {
                    sender.sendMessage(ErrorType.NO_PERMISSION)
                    return true
                }
                plugin.reloadConfig()
                sender.sendMessage("${ChatColor.GREEN}Successfully reloaded config!")
                return true
            }
            "give" -> {
                if (!sender.hasPermission(Permissions.GIVE)) {
                    sender.sendMessage(ErrorType.NO_PERMISSION)
                    return true
                }
                val player = getPlayer(sender, args) ?: return true
                val section = plugin.config.getConfigurationSection("item")
                val type = section?.getString("type")?.let(Material::matchMaterial) ?: Material.DIAMOND_PICKAXE
                val name = (section?.getString("name") ?: "&6Automatic Smelter!").formatAll(player)
                val lore = (section?.getStringList("lore") ?: listOf("&aThis pickaxe will smelt for you!"))
                    .map { it.formatAll(player) }
                val item = ItemStack(type).apply {
                    itemMeta = itemMeta?.apply {
                        displayName = name
                        setLore(lore)
                    }
                    setSpecial(true)
                }
                player.inventory.addItem(item)
                sender.sendMessage("${ChatColor.GREEN}Successfully gave item!")
                if (player.uniqueId != (sender as? Player)?.uniqueId) {
                    val text = plugin.config.getString("message-to-player-sent")?.trim()
                    if (text?.isBlank() == false) {
                        player.sendMessage(text.formatAll(player))
                    }
                }
                return true
            }
            "check" -> {
                if (!sender.hasPermission(Permissions.CHECK)) {
                    sender.sendMessage(ErrorType.NO_PERMISSION)
                    return true
                }
                val player = getPlayer(sender, args) ?: return true
                val item = player.inventory.let { inv ->
                    if (inv.itemInMainHand.type == Material.AIR) {
                        if (inv.itemInOffHand.type == Material.AIR) {
                            sender.sendMessage(ErrorType.MUST_HAVE_ITEM)
                            return true
                        }
                        return@let inv.itemInOffHand
                    }
                    return@let inv.itemInMainHand
                }
                sender.sendMessage("${ChatColor.GOLD}Auto Smelter: ${if (item.isSpecial()) "${ChatColor.GREEN}Yes" else "${ChatColor.RED}No"}")
                return true
            }
            else -> {
                sender.sendMessage(ErrorType.INVALID_ARG)
                return true
            }
        }
    }

    private fun getPlayer(sender: CommandSender, args: Array<out String>): Player? {
        val player: Player?
        if (args.size > 1) {
            player = args[1].toPlayer()
            if (player == null) {
                sender.sendMessage(ErrorType.INVALID_PLAYER)
                return null
            }
        } else {
            player = sender as? Player ?: run {
                sender.sendMessage(ErrorType.MUST_BE_PLAYER)
                return null
            }
        }
        return player
    }
}