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
import org.bukkit.event.Listener;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.skillz.commands.SkillCommand;
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
        for (PlayerSkillData data : skillData)
        {
            totalLevel += data.getLevel();
        }

        return Math.round(totalLevel / (float)skillData.size());
    }
    
}
