/**
 * bFundamentalsBuild 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
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
package uk.codingbadgers.mining;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.skillz.PlayerSkillData;
import uk.codingbadgers.skillz.SkillBase;

public class SkillMining extends SkillBase implements Listener {

	private Map<Material, Long> m_blocks = new HashMap<Material, Long>();
	
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
		
		m_blocks.put(Material.STONE, 1L);
		m_blocks.put(Material.COAL, 2L);
		m_blocks.put(Material.IRON_ORE, 5L);
		m_blocks.put(Material.REDSTONE_ORE, 10L);
		m_blocks.put(Material.LAPIS_ORE, 10L);
		m_blocks.put(Material.SANDSTONE, 1L);
		m_blocks.put(Material.DIAMOND_ORE, 20L);
		m_blocks.put(Material.GOLD_ORE, 10L);
		m_blocks.put(Material.EMERALD_ORE, 15L);
		m_blocks.put(Material.QUARTZ_ORE, 5L);
		m_blocks.put(Material.NETHERRACK, 1L);
		
	}
	
	/**
	 * 
	 * @return 
	 */
	public String getAbilityName() {
		return "Dwarven Strength";
	}
    
	/**
	 * @return The data class for this skill
	 */
	public Class<? extends PlayerData> getPlayerDataClass() {
		return SkillMiningData.class;
	}
	
	/**
	 * 
	 * @param player
	 * @param data
	 * @param event 
	 */
	@Override
	protected void onPlayerBreakBlock(FundamentalPlayer player, PlayerSkillData data, BlockBreakEvent event) {
	
		final Block block = event.getBlock();
		if (!m_blocks.containsKey(block.getType())) {
			return;
		}
		
		data.addXP(m_blocks.get(block.getType()));		
		
	}
	
	/**
	 * Handle mining ability
	 * @param event 
	 */
	@Override
	protected void onPlayerDamageBlock(FundamentalPlayer player, PlayerSkillData data, BlockDamageEvent event) {
		
		SkillMiningData skillData = (SkillMiningData)data;
		if (!skillData.isAbilityActive()) {
			return;
		}
		
		final Block block = event.getBlock();
		if (!m_blocks.containsKey(block.getType())) {
			return;
		}

		// Cancel the event and we will break the block ourselves
		event.setCancelled(true);
		
		// destroy the block
		block.breakNaturally();
		
	}
}
