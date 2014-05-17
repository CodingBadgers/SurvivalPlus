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
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.customflags.CustomFlags;
import uk.codingbadgers.skillz.skill.BlockData;
import uk.codingbadgers.skillz.skill.SkillBlockBase;
import uk.codingbadgers.skillz.skill.ToolData;
import uk.codingbadgers.skillz.skill.config.BlockConfig;

public class SkillMining extends SkillBlockBase implements Listener {

    public static BooleanFlag FLAG_ORE_REGEN = new BooleanFlag("ore-regen", RegionGroup.ALL);
    
    /**
     * 
     */
    public SkillMining() {
        super(Material.BEDROCK);
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

        RegisterTool(Material.WOOD_PICKAXE, new ToolData(1));
        RegisterTool(Material.STONE_PICKAXE, new ToolData(5));
        RegisterTool(Material.IRON_PICKAXE, new ToolData(15));
        RegisterTool(Material.GOLD_PICKAXE, new ToolData(20));
        RegisterTool(Material.DIAMOND_PICKAXE, new ToolData(30));

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

        m_customFlags = (CustomFlags) SurvivalPlus.getModuleLoader().getModule("CustomFlags");
        m_customFlags.addCustomFlag(FLAG_ORE_REGEN);
        
    }

    /**
     * @return The data class for this skill
     */
    @Override
    public Class<? extends PlayerData> getPlayerDataClass() {
        return SkillMiningData.class;
    }
}
