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

package me.dkim19375.pickaxesmelter.extension

import me.dkim19375.dkimbukkitcore.data.HelpMessage
import me.dkim19375.dkimbukkitcore.data.HelpMessageFormat
import me.dkim19375.dkimbukkitcore.function.showHelpMessage
import me.dkim19375.pickaxesmelter.PickaxeSmelter
import me.dkim19375.pickaxesmelter.enumclass.ErrorType
import me.dkim19375.pickaxesmelter.enumclass.Permissions
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permissible
import org.bukkit.plugin.java.JavaPlugin

val plugin: PickaxeSmelter by lazy { JavaPlugin.getPlugin(PickaxeSmelter::class.java) }

val format = HelpMessageFormat(header = "${ChatColor.GREEN}%name% v%version% " +
        "Help Page - [] = optional")

val commands = listOf(
    HelpMessage("help", "Shows help page", Permissions.COMMAND.perm),
    HelpMessage("give [player]", "Give yourself (or a player) a pickaxe", Permissions.GIVE.perm),
    HelpMessage("check [player]", "Check yourself (or a player) if they have the pickaxe", Permissions.CHECK.perm),
    HelpMessage("reload", "Reload the config", Permissions.RELOAD.perm)
)

fun CommandSender.sendHelpMessage(label: String) = showHelpMessage(label, null, 1, commands, plugin, format)

fun CommandSender.sendMessage(error: ErrorType) = sendMessage("${ChatColor.RED}${error.description}")

fun Permissible.hasPermission(permission: Permissions): Boolean = hasPermission(permission.perm)