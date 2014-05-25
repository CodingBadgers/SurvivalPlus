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
package uk.codingbadgers.levelprefix;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.skillz.Skillz;
import uk.codingbadgers.skillz.events.PlayerSkillLevelIncrease;

public class LevelPrefix extends Module implements Listener {

    /**
     * The skillz module
     */
    Skillz m_skillz = null;
    
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
    public void onEnable() 
    {
        register(this);
        
        m_skillz = (Skillz) SurvivalPlus.getModuleLoader().getModule("Skillz");
        if (m_skillz == null)
        {
            this.log(Level.SEVERE, "Failed to find the Skillz module.");
        }       
    }
    
    /**
     * Called after the module is loaded.
     */
    @Override
    public void onPostEnable() 
    {        
        for (Player player : Bukkit.getOnlinePlayers())
        {
            setPlayerScoreboard(player);
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
        setPlayerScoreboard(player);
    }
    
    /**
     * 
     * @param player 
     */
    private void setPlayerScoreboard(Player player)
    {
        if (m_skillz == null)
        {
            this.log(Level.SEVERE, "Failed to find the Skillz module.");
            return;
        }
        
        FundamentalPlayer funPlayer = SurvivalPlus.Players.getPlayer(player);
        if (funPlayer == null)
        {
            return;
        }
        
        Scoreboard scoreboard = player.getScoreboard();
        
        final String playerTeamName = player.getName().toLowerCase();
        Team playerTeam = scoreboard.getTeam(playerTeamName);
        if (playerTeam == null) 
        {
            playerTeam = scoreboard.registerNewTeam(playerTeamName);
        }
        playerTeam.addPlayer(player);
                
        int level = m_skillz.getPlayerLevel(funPlayer);
        String levelString = level < 10 ? "0" + level : "" + level;   
        
        playerTeam.setSuffix("");
        playerTeam.setPrefix("[Lvl " + levelString + "] ");
    }
    
    /**
     * 
     * @param event 
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void OnPlayerLevelUp(PlayerSkillLevelIncrease event)
    {
        setPlayerScoreboard(event.getPlayer().getPlayer());
    }
    
}
