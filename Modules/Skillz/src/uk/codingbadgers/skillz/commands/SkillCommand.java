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
package uk.codingbadgers.skillz.commands;

import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.commands.ModuleCommand;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.skillz.Skillz;
import uk.codingbadgers.skillz.skill.PlayerSkillData;

/**
 * @author n3wton
 */
public class SkillCommand extends ModuleCommand {

    /**
     *
     */
    private final Skillz m_skillz;

    /**
     * Class constructor
     */
    public SkillCommand(Skillz skillz) {
        super("skill", "/skill");
        m_skillz = skillz;
    }

    /**
     * Handle the command.
     *
     * @param sender  the command sender
     * @param command the command instance
     * @param label   the label of the command
     * @param args    the arguments added to the command
     * @return true, if successful, false falls through to default module
     * command handling
     * @see Module#onCommand(CommandSender, String, String[])
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            handleCommandHelp(sender);
            return true;
        }

        if (args.length == 1) {

            final String subCommand = args[0];
            if (subCommand.equalsIgnoreCase("levels")) {
                handleCommandLevels(sender);
                return true;
            }

            return true;
        }

        return true;
    }

    /**
     * @param sender
     */
    private void handleCommandHelp(CommandSender sender) {

        Module.sendMessage("Skill", sender, " - /skill levels");

    }

    /**
     * @param sender
     */
    private void handleCommandLevels(CommandSender sender) {

        if (!(sender instanceof Player)) {
            Module.sendMessage("Skill", sender, "This command can only be executed in game.");
            return;
        }

        FundamentalPlayer player = SurvivalPlus.Players.getPlayer((Player) sender);
        if (player == null) {
            Module.sendMessage("Skill", sender, "Something has gone wrong, try re-logging.");
            return;
        }

//        float totalLevel = 0;
//        
//        List<PlayerSkillData> skillData = player.getAllPlayerData("PlayerSkillData");
//        Collections.sort(skillData);        
//        for (PlayerSkillData data : skillData) {
//              
//            String skillMessage = ChatColor.GOLD + "[" + data.getSkillName() + "] " + ChatColor.WHITE;
//            if (data.getLevel() < 10) {
//                skillMessage += "0";
//            }            
//            skillMessage += data.getLevel() + "/99,    " + data.getXP() + "xp,    " + data.getXpToNextLevel() + "xp to next level";
//            
//            Module.sendMessage("Skill", sender, skillMessage);
//            totalLevel += data.getLevel();
//        }
//
//        Module.sendMessage("Skill", sender, "Total Level: " + Math.round(totalLevel / (float)skillData.size()));
        
        Scoreboard scoreboard = player.getPlayer().getScoreboard();
        final Objective objective = scoreboard.registerNewObjective("Skillz", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        float totalLevel = 0;
        List<PlayerSkillData> skillData = player.getAllPlayerData("PlayerSkillData");
        Collections.sort(skillData);   
        for (PlayerSkillData data : skillData) 
        {
            Score skillScore = objective.getScore(data.getSkillName());
            skillScore.setScore(data.getLevel());    
            totalLevel += data.getLevel();
        }        
        objective.setDisplayName(ChatColor.GOLD + "Total Level " + Math.round(totalLevel / (float)skillData.size()));
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(m_skillz.getPlugin(), 
            new Runnable() {
                @Override
                public void run() {
                    objective.unregister();
                }                
            }, 
        20L * 10);
        
    }

}
