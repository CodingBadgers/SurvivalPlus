package uk.codingbadgers.woodcutting;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.skillz.skill.SkillBlockBase;

public class SkillWoodCutting extends SkillBlockBase implements Listener {

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
		
		RegisterBlock(Material.LOG, 5L);
		
		// So the ability will work on these, but no xp is given
		RegisterBlock(Material.WOOD, 0L);
		RegisterBlock(Material.WOOD_STAIRS, 0L);
		RegisterBlock(Material.WOOD_STEP, 0L);
		RegisterBlock(Material.WOOD_DOUBLE_STEP, 0L);
		
		RegisterTool(Material.WOOD_AXE);
		RegisterTool(Material.STONE_AXE);
		RegisterTool(Material.IRON_AXE);
		RegisterTool(Material.GOLD_AXE);
		RegisterTool(Material.DIAMOND_AXE);
		
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
	 * @return The data class for this skill
	 */
	@Override
	public Class<? extends PlayerData> getPlayerDataClass() {
		return SkillWoodCuttingData.class;
	}
}