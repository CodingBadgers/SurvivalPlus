/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.skill.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.commands.ModuleCommand;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.skill.FishingConfig.FishType;
import uk.codingbadgers.skill.SkillFishing;
import uk.codingbadgers.skillz.skill.PlayerSkillData;

/**
 *
 * @author n3wton
 */
public class FishingCommand extends ModuleCommand {

    private final SkillFishing m_fishing;
    
    public FishingCommand(SkillFishing fishing) {
        super("fishing", "/fishing");
        m_fishing = fishing;
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
        
        if (!(sender instanceof Player)) {
            return true;
        }
        
        FundamentalPlayer player = SurvivalPlus.Players.getPlayer((Player)sender);
        if (player == null) {
            return true;
        }
        
        PlayerSkillData data = (PlayerSkillData) player.getPlayerData(m_fishing.getPlayerDataClass());
        if (data == null) {
            return true;
        }
        
        Comparator<FishType> comparator = new Comparator<FishType>() {
            @Override
            public int compare(FishType c1, FishType c2) {
                return c1.requiredlevel - c2.requiredlevel;
            }
        };
        
        List<FishType> fishes = new ArrayList(m_fishing.GetFish().values());
        Collections.sort(fishes, comparator);
        
        for (FishType fish : fishes) {
            ChatColor color = data.getLevel() >= fish.requiredlevel ? ChatColor.DARK_GREEN : ChatColor.DARK_RED;
            Module.sendMessage("Fishing", sender, color + fish.displayname + " | Reguired Level " + fish.requiredlevel);            
        }
                
        return true;        
    }
    
}
