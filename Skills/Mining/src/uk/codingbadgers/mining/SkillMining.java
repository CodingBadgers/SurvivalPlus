package uk.codingbadgers.mining;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
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
		
		RegisterBlock(Material.STONE, 1L);
		RegisterBlock(Material.SANDSTONE, 1L);
		RegisterBlock(Material.NETHERRACK, 1L);		
		RegisterBlock(Material.COAL_ORE, 2L);		
		RegisterBlock(Material.IRON_ORE, 5L);
		RegisterBlock(Material.QUARTZ_ORE, 5L);		
		RegisterBlock(Material.GOLD_ORE, 10L);
		RegisterBlock(Material.REDSTONE_ORE, 10L);
		RegisterBlock(Material.LAPIS_ORE, 10L);
		RegisterBlock(Material.EMERALD_ORE, 15L);		
		RegisterBlock(Material.DIAMOND_ORE, 20L);
			
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
