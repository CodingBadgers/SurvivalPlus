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
package uk.codingbadgers.skillz;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.skillz.commands.SkillCommand;
import uk.codingbadgers.skillz.events.PlayerXPIncrease;
import uk.codingbadgers.skillz.skill.PlayerSkillData;

public class Skillz extends Module implements Listener {

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
        this.register(this);
        this.registerCommand(new SkillCommand(this));        
    }

    /**
     * 
     * @param player
     * @return 
     */
    public int getPlayerLevel(FundamentalPlayer player)
    {
        List<PlayerSkillData> skillData = player.getAllPlayerData("PlayerSkillData");
        
        float totalLevel = 0;
        for (PlayerSkillData data : skillData) {
            totalLevel += data.getLevel();
        }

        return Math.round(totalLevel / (float)skillData.size());
    }
    
    /**
     * 
     * @param event 
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void OnPlayerGainXP(PlayerXPIncrease event) {
             
        final FundamentalPlayer player = event.getPlayer();
        final Scoreboard scoreboard = player.getPlayer().getScoreboard();

        if (scoreboard == null) {
            return;
        }

        final Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective == null) {
            return;
        }
        
        if (!objective.getName().equalsIgnoreCase("SkillSpecific")) {
            return;
        }
        
        final String skillName = ChatColor.stripColor(objective.getDisplayName());
        final PlayerSkillData data = event.getData();

        if (!data.getSkillName().equalsIgnoreCase(skillName)) {
            return;
        }
        
        for (String score : scoreboard.getEntries()) {
            scoreboard.resetScores(score);
        }
        
        // Level title
        Score levelTitle = objective.getScore(ChatColor.YELLOW + "Level");
        levelTitle.setScore(6);
        
        // Actual level
        Score level = objective.getScore("" + data.getLevel());
        level.setScore(5);
        
        // Progress title
        Score progressTitle = objective.getScore(ChatColor.YELLOW + "Progress");
        progressTitle.setScore(4);
        
        // Actual progress %
        int currentLevel = data.getLevel();            
        Long thisLevelXp = data.getXpForLevel(currentLevel);
        Long nextLevelXp = data.getXpForLevel(currentLevel + 1);
        Long currentXp = data.getXP();

        float scalar = 100.0f / (nextLevelXp - thisLevelXp);
        int percentComplete = (int)((currentXp - thisLevelXp) * scalar);
        
        Score progress = objective.getScore(percentComplete + "%");
        progress.setScore(3);   
        
        // Xp title
        Score xpTitle = objective.getScore(ChatColor.YELLOW + "XP");
        xpTitle.setScore(2);
        
        // Actual Xp
        Score xp = objective.getScore(data.getXP() + "xp");
        xp.setScore(1);
    }  
    
}
