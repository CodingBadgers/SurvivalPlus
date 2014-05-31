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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.skill.CraftingConfig.CraftType;
import uk.codingbadgers.skillz.skill.PlayerSkillData;
import uk.codingbadgers.skillz.skill.SkillBase;

public class SkillCrafting extends SkillBase implements Listener {
  
    Map<Material, CraftType> m_registersRecipes = new HashMap<Material, CraftType>();
    
    /**
     *
     */
    @Override
    public void onLoad() {

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
        
        final String configPath = this.getDataFolder() + File.separator + "config.json";
                
        try {
            final File file = new File(configPath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            
            Gson gson = SurvivalPlus.getGsonInstance();
            Reader reader = new FileReader(configPath);
            CraftingConfig craftConfig = gson.fromJson(reader, CraftingConfig.class);
            for (CraftType craft : craftConfig.crafttypes) {
                Material material = Material.getMaterial(craft.material.name);
                if (material != null) {
                    m_registersRecipes.put(material, craft);
                }
            }
        }
        catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to load '" + configPath + "'", ex);
        } 
        
    }

    /**
     * @return The data class for this skill
     */
    @Override
    public Class<? extends PlayerData> getPlayerDataClass() {
        return SkillCraftingData.class;
    }
    
    /**
     * 
     * @param event 
     */
    private void cancelCraft(Player player, CraftItemEvent event) {
        
        if (player != null) {
            ItemStack[] items = event.getInventory().getMatrix();
            for (ItemStack item : items) {
                if (item != null) {
                    player.getInventory().addItem(item);
                }
            }            
            
            event.getInventory().clear();
            player.updateInventory();
        }
        
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
        event.getView().close();
    }
    
    /**
     * 
     * @param event 
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void OnCraftItem(CraftItemEvent event) {
        
        List<HumanEntity> humans = event.getInventory().getViewers();
        if (humans.size() != 1) {
            cancelCraft(null, event);
            return;
        }
        
        final Player mcplayer = (Player)humans.get(0);  
        final Recipe recipe = event.getRecipe();
        final Material itemType = recipe.getResult().getType();
        
        if (!m_registersRecipes.containsKey(itemType)) {
            cancelCraft(mcplayer, event);
            return;
        }     
        
        final CraftType craftableItem = m_registersRecipes.get(itemType);
        final FundamentalPlayer player = SurvivalPlus.Players.getPlayer(mcplayer);
        if (player == null) {
            cancelCraft(mcplayer, event);
            return;
        }
        
        PlayerSkillData data = (PlayerSkillData) player.getPlayerData(this.getPlayerDataClass());
        if (data == null) {
            cancelCraft(mcplayer, event);
            return;
        }
        
        if (data.getLevel() < craftableItem.requiredlevel) {
            cancelCraft(mcplayer, event);
            return;
        }
        
        data.addXP(craftableItem.xp * recipe.getResult().getAmount());
    }

}
