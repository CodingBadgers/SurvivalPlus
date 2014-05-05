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
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.skillz.skill.BlockData;
import uk.codingbadgers.skillz.skill.SkillBlockBase;

public class SkillMining extends SkillBlockBase implements Listener {
	
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
		
		RegisterBlock(Material.STONE, new BlockData(1L, 1, SurvivalPlus.timeToTicks(0, 15)));
		RegisterBlock(Material.SANDSTONE, new BlockData(1L, 1, SurvivalPlus.timeToTicks(0, 15)));
		RegisterBlock(Material.NETHERRACK, new BlockData(1L, 1, SurvivalPlus.timeToTicks(0, 15)));		
		RegisterBlock(Material.COAL_ORE, new BlockData(2L, 5, SurvivalPlus.timeToTicks(0, 30)));		
		RegisterBlock(Material.IRON_ORE, new BlockData(5L, 10, SurvivalPlus.timeToTicks(1, 0)));
		RegisterBlock(Material.QUARTZ_ORE, new BlockData(5L, 15, SurvivalPlus.timeToTicks(1, 0)));		
		RegisterBlock(Material.GOLD_ORE, new BlockData(10L, 20, SurvivalPlus.timeToTicks(2, 30)));
		RegisterBlock(Material.REDSTONE_ORE, new BlockData(10L, 20, SurvivalPlus.timeToTicks(4, 0)));
		RegisterBlock(Material.LAPIS_ORE, new BlockData(10L, 25, SurvivalPlus.timeToTicks(5, 0)));
		RegisterBlock(Material.EMERALD_ORE, new BlockData(15L, 40, SurvivalPlus.timeToTicks(15, 0)));		
		RegisterBlock(Material.DIAMOND_ORE, new BlockData(50L, 55, SurvivalPlus.timeToTicks(30, 0)));
			
		RegisterTool(Material.WOOD_PICKAXE);
		RegisterTool(Material.STONE_PICKAXE);
		RegisterTool(Material.IRON_PICKAXE);
		RegisterTool(Material.GOLD_PICKAXE);
		RegisterTool(Material.DIAMOND_PICKAXE);
		
	}
	
	/**
	 * Get the name of this skills ability
	 * @return 
	 */
	@Override
	public String getAbilityName() {
		return "Dwarven Strength";
	}
    
	/**
	 * @return The data class for this skill
	 */
	@Override
	public Class<? extends PlayerData> getPlayerDataClass() {
		return SkillMiningData.class;
	}
}
