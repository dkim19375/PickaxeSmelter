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

import me.dkim19375.dkimcore.extension.containsIgnoreCase
import me.dkim19375.pickaxesmelter.enumclass.Permissions
import me.dkim19375.pickaxesmelter.extension.hasPermission
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.permissions.Permissible
import org.bukkit.util.StringUtil

class PickaxeTabCompleter : TabCompleter {
    private fun getPartial(token: String, collection: Iterable<String>): List<String> =
        StringUtil.copyPartialMatches(token, collection, mutableListOf())

    private fun getPartialPerm(
        token: String,
        collection: Iterable<String>,
        sender: Permissible,
        perm: Permissions
    ): List<String>? {
        if (!sender.hasPermission(perm)) {
            return null
        }
        return getPartial(token, collection)
    }

    private fun getBaseCommands(sender: Permissible): List<String> {
        val list = mutableListOf("help")
        if (sender.hasPermission(Permissions.GIVE)) {
            list.add("give")
        }
        if (sender.hasPermission(Permissions.CHECK)) {
            list.add("check")
        }
        if (sender.hasPermission(Permissions.RELOAD)) {
            list.add("reload")
        }
        return list
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>,
    ): List<String>? {
        if (!sender.hasPermission(Permissions.COMMAND)) {
            return null
        }
        return when (args.size) {
            0 -> getBaseCommands(sender)
            1 -> getPartial(args[0], getBaseCommands(sender))
            2 -> when (args[0].lowercase()) {
                "give" -> getPartialPerm(args[1], Bukkit.getOnlinePlayers()
                    .map(Player::getName), sender, Permissions.GIVE)
                "check" -> getPartialPerm(args[1], Bukkit.getOnlinePlayers()
                    .map(Player::getName), sender, Permissions.CHECK)
                else -> emptyList()
            }
            else -> emptyList()
        }
    }
}