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
package uk.codingbadgers.skill;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.skillz.skill.SkillBlockBase;

public class SkillLandscaping extends SkillBlockBase implements Listener {
	
	/**
	 * Called when the module is disabled.
	 */
    @Override
	public void onDisable() {
		
	}

	/**
	 * Called when the module is loaded.
	 */
    @Override
	public void onEnable() {
		
		RegisterBlock(Material.GRASS, 1L);
		RegisterBlock(Material.SAND, 1L);
		RegisterBlock(Material.DIRT, 1L);
		RegisterBlock(Material.GRAVEL, 1L);
		RegisterBlock(Material.SOUL_SAND, 2L);
		RegisterBlock(Material.CLAY, 2L);
			
		RegisterTool(Material.WOOD_SPADE);
		RegisterTool(Material.STONE_SPADE);
		RegisterTool(Material.IRON_SPADE);
		RegisterTool(Material.GOLD_SPADE);
		RegisterTool(Material.DIAMOND_SPADE);
		
	}
	
	/**
	 * Get the name of this skills ability
	 * @return 
	 */
	@Override
	public String getAbilityName() {
		return "Charlie Dimmock";
	}
    
	/**
	 * @return The data class for this skill
	 */
	@Override
	public Class<? extends PlayerData> getPlayerDataClass() {
		return SkillLandscapingData.class;
	}
}
