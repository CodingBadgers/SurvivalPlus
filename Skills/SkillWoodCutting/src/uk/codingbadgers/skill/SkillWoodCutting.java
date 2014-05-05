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

import org.bukkit.Material;
import org.bukkit.event.Listener;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.skillz.skill.BlockData;
import uk.codingbadgers.skillz.skill.SkillBlockBase;
import uk.codingbadgers.skillz.skill.ToolData;

public class SkillWoodCutting extends SkillBlockBase implements Listener {

    /**
     *
     */
    @Override
    public void onLoad() {

        RegisterBlock(Material.LOG, new BlockData(5L, 1, SurvivalPlus.timeToTicks(0, 15)));

        RegisterTool(Material.WOOD_AXE, new ToolData(1));
        RegisterTool(Material.STONE_AXE, new ToolData(5));
        RegisterTool(Material.IRON_AXE, new ToolData(15));
        RegisterTool(Material.GOLD_AXE, new ToolData(20));
        RegisterTool(Material.DIAMOND_AXE, new ToolData(40));

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
     * Get the name of this skills ability
     *
     * @return
     */
    @Override
    public String getAbilityName() {
        return "Tree Fucker";
    }

    /**
     * @return The data class for this skill
     */
    @Override
    public Class<? extends PlayerData> getPlayerDataClass() {
        return SkillWoodCuttingData.class;
    }
}
