/**
 * SurvivalPlus 1.0
 * Copyright (C) 2014 CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.codingbadgers.SurvivalPlus.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface GuiCallback {

    /**
     * Called when an item is click in an inventory
     *
     * @param inventory  The owner inventory
     * @param clickEvent The inventory click event
     */
    public void onClick(GuiInventory inventory, InventoryClickEvent clickEvent);

}
