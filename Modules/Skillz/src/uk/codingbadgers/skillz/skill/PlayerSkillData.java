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

import org.bukkit.ChatColor;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;

/**
 * @author n3wton
 */
public class PlayerSkillData implements PlayerData, Comparable {

    /**
     * The name of the skill data
     */
    private final String m_skillDataName;

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
     * The time the ability was activated
     */
    private Long m_abilityActivateTime = 0L;

    /**
     * Get the current level of xp
     *
     * @return The current level of xp
     */
    public Long getXP() {
        return m_xp;
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
        final int oldLevel = this.getLevel();
        m_xp += amount;

        final int level = this.getLevel();
        if (oldLevel != level) {
            FundamentalPlayer player = SurvivalPlus.getDataOwner(this);
            if (player != null) {
                player.sendMessage(ChatColor.YELLOW + "Your " + ChatColor.GOLD + this.getSkillName() + ChatColor.YELLOW
                        + " skill has increased to level " + ChatColor.GOLD + level + ChatColor.YELLOW + "!");
            }
        }
    }

    /**
     * @return
     */
    public boolean enableAbility() {

        if (isAbilityActive()) {
            return true;
        }

        final Long currentTime = System.currentTimeMillis();
        final Long timeSinceLast = currentTime - m_abilityActivateTime - getAbilityLength();

        if (m_abilityActivateTime == 0 || timeSinceLast > getAbilityCooldown()) {
            m_abilityActivateTime = currentTime;
            return true;
        }

        return false;
    }

    /**
     * @return
     */
    public Long getTimeUntilAbilityRefresh() {
        final Long currentTime = System.currentTimeMillis();
        final Long timeSinceLast = currentTime - m_abilityActivateTime - getAbilityLength();
        return getAbilityCooldown() - timeSinceLast;
    }

    /**
     * @return
     */
    public Long getAbilityCooldown() {
        return 10L * 60L * 1000L;
    }

    /**
     * @return
     */
    public Long getAbilityLength() {
        return 4000L + (getLevel() * 1000L);
    }

    /**
     * @return
     */
    public boolean isAbilityActive() {
        Long timeLeft = System.currentTimeMillis() - m_abilityActivateTime;
        return timeLeft < getAbilityLength();
    }

    /**
     * 
     * @return
     */
    public int getLevel() {
        return (int) ((Math.pow(m_xp, 0.5) * 0.3) + 1);
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
}
