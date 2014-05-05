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
import uk.codingbadgers.skillz.PlayerSkillData;
import uk.codingbadgers.skillz.SkillBase;

/**
 *
 * @author n3wton
 */
public abstract class SkillBlockBase extends SkillBase {
	
	/**
	 * The blocks that are used by this skill
	 */
	private final Map<Material, Long> m_blocks = new EnumMap<Material, Long>(Material.class);
	
	/**
	 * The tools that have the mining ability
	 */
	private final List<Material> m_tools = new ArrayList<Material>();
	
	/**
	 * Register a block with this skill and the amount of xp gained for breaking said block
	 * @param block The material of the block to register
	 * @param xp The amount of xp to be given when this block is broken
	 */
	protected void RegisterBlock(Material block, Long xp) {
		m_blocks.put(block, xp);
	}
	
	/**
	 * Register a tool which can be used with this skills ability
	 * @param tool The material of the tool to register
	 */
	protected void RegisterTool(Material tool) {
		m_tools.add(tool);
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
			data.addXP(m_blocks.get(block.getType()));		
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
			data.addXP(m_blocks.get(block.getType()));	
		}
		
		// Cancel the event and we will break the block ourselves
		event.setCancelled(true);
		
		// destroy the block
		block.breakNaturally();
				
	}
}
