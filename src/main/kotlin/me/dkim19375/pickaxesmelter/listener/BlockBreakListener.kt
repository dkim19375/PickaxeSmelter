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

package me.dkim19375.pickaxesmelter.listener

import me.dkim19375.pickaxesmelter.extension.isSpecial
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class BlockBreakListener : Listener {
    private val blockMap = mapOf(
        Material.COBBLESTONE to Material.STONE,
        Material.IRON_ORE to Material.IRON_INGOT,
        Material.GOLD_ORE to Material.GOLD_INGOT
    )

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private fun BlockBreakEvent.onBreak() {
        val item = player.inventory.itemInMainHand
        if (!item.isSpecial()) {
            return
        }
        val drops = block.getDrops(item)
        val newDrops = drops.map {
            val new = blockMap[it.type]
            new?.let { n -> ItemStack(n) } ?: it
        }
        if (drops == newDrops) {
            return
        }
        isDropItems = false
        newDrops.forEach {
            block.world.dropItem(block.location, it)
        }
    }
}