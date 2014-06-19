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
package uk.codingbadgers.skillz.skill;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.skillz.events.PlayerSkillLevelIncrease;
import uk.codingbadgers.skillz.events.PlayerXPIncrease;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;
import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.DatabaseTable;

/**
 * @author n3wton
 */
public abstract class PlayerSkillData implements PlayerData, Comparable {
    
    /**
     * The database table for this player data
     */
    private DatabaseTable m_databaseTable = null;
    
    /**
     * The name of the skill data
     */
    private final String m_skillDataName;

    @Override
    public void onEnable() {
        
        BukkitDatabase dbase = SurvivalPlus.getBukkitDatabase();
        
        final String tableName = "SurvivalPlus-Skill" + getSkillName();
        m_databaseTable = dbase.createTable(tableName, SerializablePlayerData.class);
    }

    @Override
    public void onDisable() {

    }
    
    /**
     * @param skillName
     */
    public PlayerSkillData(String skillName) {
        m_skillDataName = skillName;
    }

    /**
     * Get the name of the skill for this data
     *
     * @return
     */
    public String getSkillName() {
        return m_skillDataName;
    }

    /**
     * The current level of xp for this skill
     */
    private Long m_xp = 0L;

    /**
     * Get the current level of xp
     *
     * @return The current level of xp
     */
    public Long getXP() {
        return m_xp;
    }

    /**
     * 
     * @return 
     */
    public Long getXpToNextLevel() {
        return getXpForLevel(this.getLevel() + 1) - m_xp;
    }
    
    /**
     * Set the xp level
     *
     * @param xp The new xp level
     */
    public void setXP(Long xp) {
        m_xp = xp;
    }

    /**
     * Add a given amount of xp
     *
     * @param amount The amount to add (can be negative)
     */
    public void addXP(Long amount) {
        final FundamentalPlayer player = SurvivalPlus.getDataOwner(this);
        
        final int oldLevel = this.getLevel();
        m_xp += amount;
        
        Bukkit.getPluginManager().callEvent(new PlayerXPIncrease(player, this));
        
        final int level = this.getLevel();
        if (oldLevel != level) {
            player.sendMessage(ChatColor.YELLOW + "Your " + ChatColor.GOLD + this.getSkillName() + ChatColor.YELLOW
                    + " skill has increased to level " + ChatColor.GOLD + level + ChatColor.YELLOW + "!");

            Bukkit.getPluginManager().callEvent(new PlayerSkillLevelIncrease(player, this));
        }
        
        saveData(player);
    }
    
    /**
     * Save the data to a database
     */
    private void saveData(FundamentalPlayer player) {

        SerializablePlayerData data = new SerializablePlayerData();
        data.PlayerUUID = player.getPlayer().getUniqueId().toString();
        data.Xp = m_xp;
        
        if (m_databaseTable.exists("PlayerUUID=" + data.PlayerUUID))
        {
            m_databaseTable.update(data, SerializablePlayerData.class, "PlayerUUID=" + data.PlayerUUID, false);
        }
        else
        {
            m_databaseTable.insert(data, SerializablePlayerData.class, false);
        }
                
    }

    /**
     * 
     * @return
     */
    public int getLevel() {
        return (int) ((Math.pow(m_xp, 0.5) * 0.3) + 1);
    }

    /**
     * 
     * @return
     */
    public Long getXpForLevel(int level) {
        return (long) Math.pow((level - 1) / 0.3f, 2.0f);
    }
    
    /**
     * Implement alphabetic sorting
     * @param other The data to compare against
     * @return 
     */
    @Override
    public int compareTo(Object other) {
        PlayerSkillData skillData = (PlayerSkillData) other;
        return getSkillName().compareTo(skillData.getSkillName());
    }

    @Override
    public String getGroup() {
        return "PlayerSkillData";
    }
}
