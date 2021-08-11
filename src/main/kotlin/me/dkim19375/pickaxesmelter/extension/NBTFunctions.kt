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

import de.tr7zw.nbtapi.NBTItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

private const val KEY = "PickaxeSmelter-Special-Pickaxe"

fun ItemStack.getNBT(): NBTItem? = if (type == Material.AIR) null else NBTItem(this, true)

fun ItemStack.isSpecial(): Boolean = getNBT()?.keys?.contains(KEY) == true

fun ItemStack.setSpecial(special: Boolean) = if (special) {
    getNBT()?.setByte(KEY, 0)
} else {
    getNBT()?.removeKey(KEY)
}