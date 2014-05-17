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

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.logging.Level;
import org.bukkit.Bukkit;
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
import uk.codingbadgers.skillz.skill.config.BlockConfig;

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

        final String configPath = this.getDataFolder() + File.separator + "block-config.json";
                
        try {
            final File file = new File(configPath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            
            Gson gson = SurvivalPlus.getGsonInstance();
            Reader reader = new FileReader(configPath);
            BlockConfig blockConfig = gson.fromJson(reader, BlockConfig.class);
            
            for (BlockConfig.Block block : blockConfig.blocks) {
                
                Material material = Material.matchMaterial(block.material);
                if (material != null) {
                    BlockData blockdata = new BlockData(
                            block.xp, 
                            block.requiredlevel, 
                            SurvivalPlus.timeToTicks(block.regentime_minutes, block.regentime_seconds),
                            block.dataid);
                    RegisterBlock(material, blockdata, block.dataid);
                }                
            }
        }
        catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to load '" + configPath + "'", ex);
        } 
        
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
