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

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.skillz.skill.BlockData;
import uk.codingbadgers.skillz.skill.SkillBlockBase;
import uk.codingbadgers.skillz.skill.ToolData;

public class SkillWoodCutting extends SkillBlockBase implements Listener {

    /**
     * 
     */
    public SkillWoodCutting() {
        super(Material.FENCE);
    }
    
    /**
     *
     */
    @Override
    public void onLoad() {

        Map<Byte, BlockData> logData = new HashMap<Byte, BlockData>();
        logData.put((byte)0, new BlockData(5L, 1, SurvivalPlus.timeToTicks(0, 15))); // Oak
        logData.put((byte)1, new BlockData(15L, 10, SurvivalPlus.timeToTicks(0, 25))); // Spruce
        logData.put((byte)2, new BlockData(35L, 25, SurvivalPlus.timeToTicks(0, 45))); // Birch
        logData.put((byte)3, new BlockData(80L, 55, SurvivalPlus.timeToTicks(0, 90))); // Jungle        
        RegisterBlock(Material.LOG, logData);
        
        Map<Byte, BlockData> log2Data = new HashMap<Byte, BlockData>();
        log2Data.put((byte)0, new BlockData(25L, 15, SurvivalPlus.timeToTicks(0, 35))); // Acaicia
        log2Data.put((byte)1, new BlockData(100L, 70, SurvivalPlus.timeToTicks(0, 120))); // Dark Oak    
        RegisterBlock(Material.LOG_2, log2Data);

        RegisterTool(Material.WOOD_AXE, new ToolData(1));
        RegisterTool(Material.STONE_AXE, new ToolData(5));
        RegisterTool(Material.IRON_AXE, new ToolData(15));
        RegisterTool(Material.GOLD_AXE, new ToolData(20));
        RegisterTool(Material.DIAMOND_AXE, new ToolData(40));

    }

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

    }

    /**
     * @return The data class for this skill
     */
    @Override
    public Class<? extends PlayerData> getPlayerDataClass() {
        return SkillWoodCuttingData.class;
    }
    
    /**
     * @param event
    */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onLeafDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }
}
