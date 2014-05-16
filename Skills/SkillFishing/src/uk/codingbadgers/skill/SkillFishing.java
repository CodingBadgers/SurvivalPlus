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

import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.SetFlag;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.customflags.CustomFlags;
import uk.codingbadgers.skillz.skill.PlayerSkillData;
import uk.codingbadgers.skillz.skill.SkillBase;

public class SkillFishing extends SkillBase implements Listener {

    public static SetFlag<String> FLAG_FISHING_SPOT_TYPE = new SetFlag<String>("fishing-spot-type", RegionGroup.ALL, new FishFlag(null));
    
    private Map<String, ItemStack> m_registeredFish = new HashMap<String, ItemStack>();
        
    /**
     * 
     */
    public SkillFishing() {
    }
    
    /**
     *
     */
    @Override
    public void onLoad() {

        ItemStack trout = new ItemStack(Material.RAW_FISH);
        ItemMeta troutData = trout.getItemMeta();
        troutData.setDisplayName("Raw Trout");
        trout.setItemMeta(troutData);
        m_registeredFish.put("trout", trout);
        
        ItemStack mackerel = new ItemStack(Material.RAW_FISH);
        ItemMeta mackerelData = mackerel.getItemMeta();
        mackerelData.setDisplayName("Raw Mackerel");
        mackerel.setItemMeta(mackerelData);
        m_registeredFish.put("mackerel", mackerel);
        
        ItemStack cod = new ItemStack(Material.RAW_FISH);
        ItemMeta codData = cod.getItemMeta();
        codData.setDisplayName("Raw Cod");
        cod.setItemMeta(codData);
        m_registeredFish.put("cod", cod);
        
        ItemStack bass = new ItemStack(Material.RAW_FISH);
        ItemMeta bassData = bass.getItemMeta();
        bassData.setDisplayName("Raw Bass");
        bass.setItemMeta(bassData);
        m_registeredFish.put("bass", bass);
        
        ItemStack salmon = new ItemStack(Material.RAW_FISH, 1, (short)1, (byte)1);
        m_registeredFish.put("salmon", salmon);
        
        ItemStack redcod = new ItemStack(Material.RAW_FISH, 1, (short)1, (byte)1);
        ItemMeta redcodData = redcod.getItemMeta();
        redcodData.setDisplayName("Raw Redcod");
        redcod.setItemMeta(redcodData);
        m_registeredFish.put("redcod", redcod);
        
        ItemStack clownfish = new ItemStack(Material.RAW_FISH, 1, (short)2, (byte)2);
        m_registeredFish.put("clownfish", clownfish);
        
        ItemStack angelfish = new ItemStack(Material.RAW_FISH, 1, (short)2, (byte)2);
        ItemMeta angelData = angelfish.getItemMeta();
        angelData.setDisplayName("Raw Angelfish");
        angelfish.setItemMeta(angelData);      
        m_registeredFish.put("angelfish", angelfish);
        
        ItemStack pufferfish = new ItemStack(Material.RAW_FISH, 1, (short)3, (byte)3);
        m_registeredFish.put("pufferfish", pufferfish);    
        
        ItemStack boxfish = new ItemStack(Material.RAW_FISH, 1, (short)3, (byte)3);
        ItemMeta boxfishData = boxfish.getItemMeta();
        boxfishData.setDisplayName("Raw Boxfish");
        boxfish.setItemMeta(boxfishData);  
        m_registeredFish.put("boxfish", boxfish);
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

            Random random = new Random();            
            Object[] fishes = fishSet.toArray();
            String fishType = ((String)fishes[random.nextInt(fishes.length)]).toLowerCase();     
 
            if (!m_registeredFish.containsKey(fishType)) {
                return;
            }
            
            Module.sendMessage("Fishing", player, "You caught a " + fishType + ".");
            
            final PlayerInventory invent = player.getInventory();
            invent.addItem(m_registeredFish.get(fishType).clone());
            player.updateInventory();
                          
            FundamentalPlayer fundamentalPlayer = SurvivalPlus.Players.getPlayer(player);
            if (fundamentalPlayer == null) {
                return;
            }

            PlayerSkillData data = (PlayerSkillData) fundamentalPlayer.getPlayerData(this.getPlayerDataClass());
            if (data == null) {
                return;
            }
  
            data.addXP(10L);            
        }
        
    }
}
