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
package uk.codingbadgers.skillz.skill;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;

/**
 *
 * @author n3wton
 */
public abstract class SkillBlockBase extends SkillBase {
	
	/**
	 * The blocks that are used by this skill
	 */
	private final Map<Material, BlockData> m_blocks = new EnumMap<Material, BlockData>(Material.class);
	
	/**
	 * The tools that have the mining ability
	 */
	private final List<Material> m_tools = new ArrayList<Material>();
	
	/**
	 * Register a block with this skill and the amount of xp gained for breaking said block
	 * @param block The material of the block to register
	 * @param xp The amount of xp to be given when this block is broken
	 */
	protected void RegisterBlock(Material block, BlockData blockData) {
		m_blocks.put(block, blockData);
	}
	
	/**
	 * Register a tool which can be used with this skills ability
	 * @param tool The material of the tool to register
	 */
	protected void RegisterTool(Material tool) {
		m_tools.add(tool);
	}
	
	/**
	 * 
	 * @return 
	 */
	public Map<Material, BlockData> getBlocks() {
		return m_blocks;
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
		
		if (!m_tools.contains(item.getType())) {
			return false;
		}
		
		final Block block = event.getClickedBlock();
		if (block == null) {
			return false;
		}
		
		return m_blocks.containsKey(block.getType());
	}
	
	/**
	 * Called when a player breaks a block
	 * @param player The player who broke a block
	 * @param data The players skill data for this skill
	 * @param event The Bukkit block break event
	 * @param placedByPlayer Was the block in this event placed by a player
	 */
	@Override
	protected void onPlayerBreakBlock(FundamentalPlayer player, PlayerSkillData data, BlockBreakEvent event, boolean placedByPlayer) {
	
		final Block block = event.getBlock();
		if (!m_blocks.containsKey(block.getType())) {
			return;
		}
		
		if (!placedByPlayer) {
			data.addXP(m_blocks.get(block.getType()).getXp());		
		}
		
	}

	/**
	 * Handle block based skill ability
	 * @param player The player who damaged the block
	 * @param data The data for the player for this skill
	 * @param event The Bukkit block damage event
	 * @param placedByPlayer Was the block in this event placed by a player
	 */
	@Override
	protected void onPlayerDamageBlock(FundamentalPlayer player, PlayerSkillData data, BlockDamageEvent event, boolean placedByPlayer) {
		
		if (!data.isAbilityActive()) {
			return;
		}
		
		final Block block = event.getBlock();
		if (!m_blocks.containsKey(block.getType())) {
			return;
		}

		if (!placedByPlayer) {
			// Give them then xp
			data.addXP(m_blocks.get(block.getType()).getXp());	
		}
		
		// Cancel the event and we will break the block ourselves
		event.setCancelled(true);
		
		// destroy the block
		block.breakNaturally();
				
	}
	
	/**
	 * 
	 * @param player
	 * @param block 
	 * @return  
	 */
	public boolean canUseBlock(FundamentalPlayer player, Block block) {
		
		if (!m_blocks.containsKey(block.getType())) {
			return false;
		}
		
		final BlockData blockData = m_blocks.get(block.getType());
		final PlayerSkillData playerData = (PlayerSkillData)player.getPlayerData(this.getPlayerDataClass());
		
		return playerData.getLevel() >= blockData.getMinimumLevel();		
	}
}
