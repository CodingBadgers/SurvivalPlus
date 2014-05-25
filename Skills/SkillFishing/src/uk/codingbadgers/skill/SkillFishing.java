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
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.SetFlag;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.PlayerInventory;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.customflags.CustomFlags;
import uk.codingbadgers.skill.FishingConfig.FishType;
import uk.codingbadgers.skill.commands.FishingCommand;
import uk.codingbadgers.skillz.skill.PlayerSkillData;
import uk.codingbadgers.skillz.skill.SkillBase;

public class SkillFishing extends SkillBase implements Listener {

    public static SetFlag<String> FLAG_FISHING_SPOT_TYPE = new SetFlag<String>("fishing-spot-type", RegionGroup.ALL, new FishFlag(null));
    
    private Map<String, FishType> m_registeredFish = new HashMap<String, FishType>();
        
    /**
     * 
     */
    public SkillFishing() {
    }
    
    /**
     * 
     * @return 
     */
    public Map<String, FishType> GetFish() {
        return m_registeredFish;      
    }
    
    /**
     *
     */
    @Override
    public void onLoad() {

        final String configPath = this.getDataFolder() + File.separator + "config.json";
                
        try {
            final File file = new File(configPath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            
            Gson gson = SurvivalPlus.getGsonInstance();
            Reader reader = new FileReader(configPath);
            FishingConfig fishingConfig = gson.fromJson(reader, FishingConfig.class);
            for (FishType fish : fishingConfig.fishtypes) {
                registerFish(fish);
            }
        }
        catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to load '" + configPath + "'", ex);
        } 
    }

    private void registerFish(FishType fish) {
        
        if (fish.setup()) {
            m_registeredFish.put(fish.name, fish);
        }
               
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
        m_customFlags.addCustomFlag(FLAG_FISHING_SPOT_TYPE);
 
        registerCommand(new FishingCommand(this));        
    }

    /**
     * @return The data class for this skill
     */
    @Override
    public Class<? extends PlayerData> getPlayerDataClass() {
        return SkillFishingData.class;
    }
    
    /**
     * 
     * @param event 
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void OnPlayerFish(PlayerFishEvent event) {
     
        final Player player = event.getPlayer();
        final PlayerFishEvent.State state = event.getState();
        final Entity caught = event.getCaught();
        
        if (caught != null && !(caught instanceof LivingEntity)) {
            caught.remove();
        }
        event.setExpToDrop(0);
      
        Set<String> fishSet = (Set<String>) m_customFlags.getFlag(player, FLAG_FISHING_SPOT_TYPE);

        if (state == PlayerFishEvent.State.FISHING) {
            if (fishSet == null) { 
                Module.sendMessage("Fishing", player, "There arn't any fishes here, try fishing at a fishing spot!");
                event.setCancelled(true);
            }
            return;
        }
        
        if (state == PlayerFishEvent.State.CAUGHT_FISH) {

            FundamentalPlayer fundamentalPlayer = SurvivalPlus.Players.getPlayer(player);
            if (fundamentalPlayer == null) {
                return;
            }

            PlayerSkillData data = (PlayerSkillData) fundamentalPlayer.getPlayerData(this.getPlayerDataClass());
            if (data == null) {
                return;
            }

            Random random = new Random();            
            Object[] allfishes = fishSet.toArray();
            
            // Check minimum level
            List<String> fishes = new ArrayList<String>();
            for (Object fishObject : allfishes) {
                String fishName = (String)fishObject;
                if (m_registeredFish.containsKey(fishName)) {
                    FishType fish = m_registeredFish.get(fishName);
                    if (data.getLevel() >= fish.requiredlevel) {
                        fishes.add(fishName);
                    }                    
                }
            }

            if (fishes.isEmpty()) {
                Module.sendMessage("Fishing", player, "The fish got away, you need to be a higher level to catch the fish around here.");
                return;
            }            
            
            String fishType = fishes.get(random.nextInt(fishes.size())).toLowerCase();     
 
            Module.sendMessage("Fishing", player, "You caught a " + fishType + ".");
            
            FishType fish = m_registeredFish.get(fishType);
            
            final PlayerInventory invent = player.getInventory();
            invent.addItem(fish.getFish().clone());
            player.updateInventory();
                            
            data.addXP(fish.xp);            
        }
        
    }
}
