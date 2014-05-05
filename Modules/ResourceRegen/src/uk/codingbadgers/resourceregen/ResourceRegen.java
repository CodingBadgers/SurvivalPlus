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
package uk.codingbadgers.resourceregen;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.skillz.Skillz;
import uk.codingbadgers.skillz.skill.SkillBlockBase;

public class ResourceRegen extends Module implements Listener {

	/**
	 * 
	 */
	private Skillz m_skillz = null;
	
	/**
	 * 
	 */
	private final Map<Material, Long> m_blocks = new EnumMap<Material, Long>(Material.class);
	
	/**
	 * 
	 */
	private final Map<Block, Material> m_blocksToRegen = new HashMap<Block, Material>();
	
	/**
	 * Called when the module is disabled.
	 */
    @Override
	public void onDisable() {
	
		for (Entry<Block, Material> entry : m_blocksToRegen.entrySet()) {
			final Block block = entry.getKey();
			final Material type = entry.getValue();
			block.setType(type);			
		}
		
	}

	/**
	 * Called when the module is loaded.
	 */
    @Override
	public void onEnable() {
		
		m_skillz = (Skillz)m_plugin.getModuleInstance(Skillz.class);
		
		register(this);
		
		final Long oneMinute = 20L * 60L;
		m_blocks.put(Material.STONE, (long)(oneMinute * 0.5));
		m_blocks.put(Material.COAL_ORE, oneMinute * 1L);
		m_blocks.put(Material.IRON_ORE, oneMinute * 2L);
		m_blocks.put(Material.GOLD_ORE, oneMinute * 3L);
		m_blocks.put(Material.EMERALD_ORE, oneMinute * 5L);
		m_blocks.put(Material.DIAMOND_ORE, oneMinute * 10L);
				
	}
	
	/**
	 * 
	 * @param event 
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		
		// If the player is in creative, let them do what they want
		final Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		
		// Always cancel an event if not in creative
		event.setCancelled(true);
				
		final Block block = event.getBlock();
		final Material type = block.getType();
		
		// If this block isn't registered just cancel the event
		if (!m_blocks.containsKey(type)) {
			return;
		}
		
		// Give the player the drops directly
		final Inventory playerInvent = player.getInventory();
		Collection<ItemStack> drops = block.getDrops();
		for (ItemStack drop : drops) {
			playerInvent.addItem(drop);
		}
		
		// Set the block to bedrock
		block.setType(Material.BEDROCK);
		m_blocksToRegen.put(block, type);
		
		// Start a timer for when the block should regen
		Bukkit.getScheduler().scheduleSyncDelayedTask(
			m_plugin, 
			new Runnable() {
				@Override
				public void run() {
					block.setType(type);
					m_blocksToRegen.remove(block);
				}
			}, 
			m_blocks.get(type));
		
	}
	
	/**
	 * 
	 * @param event 
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockDamage(BlockDamageEvent event) {
		
		// If the player is in creative, let them do what they want
		final Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		
		final FundamentalPlayer survivalPlayer = SurvivalPlus.Players.getPlayer(player);
		if (survivalPlayer == null) {
			return;
		}
		
		final Block block = event.getBlock();
		
		List<SkillBlockBase> blockSkills = m_plugin.getModuleInstances(SkillBlockBase.class);
		for (SkillBlockBase skill : blockSkills) {
			if (skill.canUseBlock(survivalPlayer, block)) {
				return;
			}
		}
		
		if (block.getType() != Material.BEDROCK) {
			survivalPlayer.sendMessage("You are not a high enough level to break this...");
		}
		
		// No skills alowed the use of that block for the player, cancel
		event.setCancelled(true);
	}

}
