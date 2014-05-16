/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.skill;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import org.bukkit.command.CommandSender;

/**
 *
 * @author n3wton
 */
public class FishFlag extends Flag<String> {

    public FishFlag(String name, RegionGroup defaultGroup) {
        super(name, defaultGroup);
    }
    
    public FishFlag(String name) {
        super(name);
    }

    @Override
    public String parseInput(WorldGuardPlugin wgp, CommandSender cs, String input) throws InvalidFlagFormat {
        return input;
    }

    @Override
    public String unmarshal(Object o) {
        if (o instanceof String) {
            return (String) o;
        } else {
            return null;
        }
    }

    @Override
    public Object marshal(String t) {
        return t;
    }
    
}
