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

import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.skillz.skill.PlayerSkillData;
import uk.codingbadgers.skillz.skill.SkillBase;

public class SkillDefence extends SkillBase implements Listener {

    public static BooleanFlag FLAG_ORE_REGEN = new BooleanFlag("ore-regen", RegionGroup.ALL);
    
    /**
     * 
     */
    public SkillDefence() {

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
        return SkillDefenceData.class;
    }
    
    /**
     * 
     * @param event 
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void OnEntityDamanged(EntityDamageByEntityEvent event)
    {
        final Entity damager = event.getEntity();
        if (!(damager instanceof Player)) {
            return;
        }

        FundamentalPlayer player = SurvivalPlus.Players.getPlayer((Player)damager);
        if (player == null) {
            return;
        }

        PlayerSkillData data = (PlayerSkillData) player.getPlayerData(this.getPlayerDataClass());
        if (data == null) {
            return;
        }
        
        final Double damage = event.getDamage();
        data.addXP(damage.longValue());
    }
}
