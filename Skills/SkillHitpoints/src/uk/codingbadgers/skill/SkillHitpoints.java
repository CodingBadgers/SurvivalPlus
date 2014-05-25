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

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.projectiles.ProjectileSource;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.skillz.skill.PlayerSkillData;
import uk.codingbadgers.skillz.skill.SkillBase;

public class SkillHitpoints extends SkillBase implements Listener {

    /**
     * 
     */
    public SkillHitpoints() {

    }
    
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

    }

    /**
     * @return The data class for this skill
     */
    @Override
    public Class<? extends PlayerData> getPlayerDataClass() {
        return SkillHitpointsData.class;
    }
    
        /**
     * Called after the module is loaded.
     */
    @Override
    public void onPostEnable() 
    {        
        for (Player player : Bukkit.getOnlinePlayers())
        {
            final FundamentalPlayer funplayer = SurvivalPlus.Players.getPlayer(player);
            if (funplayer == null) {
                return;
            }

            PlayerSkillData data = (PlayerSkillData) funplayer.getPlayerData(this.getPlayerDataClass());
            if (data == null) {
                return;
            }    

            final int level = data.getLevel();
            setPlayerHitpoints(player, level);
        }               
    }
    
    /**
     * 
     * @param event 
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void OnPlayerLogin(PlayerLoginEvent event) 
    {    
        final Player player = event.getPlayer();
        final FundamentalPlayer funplayer = SurvivalPlus.Players.getPlayer(player);
        if (funplayer == null) {
            return;
        }
        
        PlayerSkillData data = (PlayerSkillData) funplayer.getPlayerData(this.getPlayerDataClass());
        if (data == null) {
            return;
        }    
        
        final int level = data.getLevel();
        setPlayerHitpoints(player, level);
    }
    
    /**
     * 
     * @param event 
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void OnEntityDamanged(EntityDamageByEntityEvent event) {

        final Entity entity = event.getEntity();
        final Entity damager = event.getDamager();
        FundamentalPlayer player = null;
        
        if (entity instanceof Player) {
            // Defence
            player = SurvivalPlus.Players.getPlayer((Player) entity);
        }
        else if (damager instanceof Player) {
            // Attack
            player = SurvivalPlus.Players.getPlayer((Player) damager);
        }
        else if (damager instanceof Projectile) {
            // Archery
            final ProjectileSource source = ((Projectile)damager).getShooter();
            if (source instanceof Player) {
                player = SurvivalPlus.Players.getPlayer((Player) source);
            }
        }
        
        if (player == null) {
            return;
        }
        
        PlayerSkillData data = (PlayerSkillData) player.getPlayerData(this.getPlayerDataClass());
        if (data == null) {
            return;
        }
        
        final int oldlevel = data.getLevel();
        data.addXP(1L);        
        final int level = data.getLevel();
        
        // Have we leveld up? give more health
        if (level != oldlevel) {            
            setPlayerHitpoints(player.getPlayer(), level);            
        }
    }
    
    /**
     * 
     * @param player
     * @param level 
     */
    private void setPlayerHitpoints(Player player, int level) {

        final float maxHealth = 60;
        final float defaultHealth = 20;
        
        float hitpointScale = ((maxHealth - defaultHealth) / maxHealth); 
        int additionalHitpoints = (int)(hitpointScale * level);

        player.setMaxHealth(defaultHealth + additionalHitpoints);
        player.setHealth(defaultHealth + additionalHitpoints);
    }
}
