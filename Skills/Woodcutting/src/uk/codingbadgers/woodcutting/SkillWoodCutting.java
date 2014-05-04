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
package uk.codingbadgers.woodcutting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.skillz.PlayerSkillData;
import uk.codingbadgers.skillz.SkillBase;

public class SkillWoodCutting extends SkillBase implements Listener {

	/**
	 * The blocks that are used by the mining skill
	 */
	private Map<Material, Long> m_blocks = new HashMap<Material, Long>();
	
	/**
	 * The axes that have the mining ability
	 */
	private List<Material> m_axes = new ArrayList<Material>();
	
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
		
		m_blocks.put(Material.LOG, 5L);
			
		m_axes.add(Material.WOOD_AXE);
		m_axes.add(Material.STONE_AXE);
		m_axes.add(Material.IRON_AXE);
		m_axes.add(Material.GOLD_AXE);
		m_axes.add(Material.DIAMOND_AXE);
		
	}
	
	/**
	 * Get the name of this skills ability
	 * @return 
	 */
	@Override
	public String getAbilityName() {
		return "Tree Fucker";
	}
	
	/**
	 * See if the skill will allow the ability to activate
	 * @param event The player interact event
	 * @return True if it can be used, false otherwise
	 */
	@Override
	public boolean canActivateAbility(PlayerInteractEvent event) {	
		
		final Player player = event.getPlayer();
		final ItemStack item = player.getItemInHand();
		
		if (!m_axes.contains(item.getType())) {
			return false;
		}
		
		final Block block = event.getClickedBlock();
		if (block == null) {
			return false;
		}
		
		return m_blocks.containsKey(block.getType());
	}
    
	/**
	 * @return The data class for this skill
	 */
	@Override
	public Class<? extends PlayerData> getPlayerDataClass() {
		return SkillWoodCuttingData.class;
	}
	
	/**
	 * Called when a player breaks a block
	 * @param player The player who broke a block
	 * @param data The players skill data for this skill
	 * @param event The Bukkit block break event
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
		
		SkillWoodCuttingData skillData = (SkillWoodCuttingData)data;
		if (!skillData.isAbilityActive()) {
			return;
		}
		
		final Block block = event.getBlock();
		if (!m_blocks.containsKey(block.getType())) {
			return;
		}

		// Give them then xp
		data.addXP(m_blocks.get(block.getType()));	
		
		// Cancel the event and we will break the block ourselves
		event.setCancelled(true);
		
		// destroy the block
		block.breakNaturally();
				
	}
}
