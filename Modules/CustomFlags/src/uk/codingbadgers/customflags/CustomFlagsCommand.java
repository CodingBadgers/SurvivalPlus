/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.codingbadgers.customflags;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.EnumFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.SetFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.SurvivalPlus.commands.ModuleCommand;
import uk.codingbadgers.customflags.flags.CustomSetFlag;
import uk.codingbadgers.customflags.util.ClassHacker;

/**
 *
 * @author n3wton
 */
public class CustomFlagsCommand extends ModuleCommand {

    public CustomFlagsCommand() {
        super("flags", "/flags");
    }

    /**
     * Handle the command.
     *
     * @param sender the command sender
     * @param command the command instance
     * @param label the label of the command
     * @param args the arguments added to the command
     * @return true, if successful, false falls through to default module
     * command handling
     * @see Module#onCommand(CommandSender, String, String[])
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1) {
            sendFlagList(sender, false);
            return true;
        } else {
            if (args[0].equalsIgnoreCase("-d")) {
                sendFlagList(sender, true);
                return true;
            } else if (args[0].equalsIgnoreCase("-i") && args.length > 1) {
                Flag<?> flag = FlagManager.getCustomFlag(args[1]);
                if (flag == null) {
                    for (Flag<?> flag2 : DefaultFlag.getFlags()) {
                        if (flag2.getName().equalsIgnoreCase(args[1])) {
                            flag = flag2;
                            break;
                        }
                    }
                    if (flag == null) {
                        sender.sendMessage(ChatColor.RED + "There is no flag with this name.");
                        return true;
                    }
                }
                sender.sendMessage(ChatColor.YELLOW + "Flag \"" + flag.getName() + "\":");
                sender.sendMessage(ChatColor.GRAY + (FlagManager.customFlags.containsKey(flag.getName()) ? "Custom" : "Default") + " flag");
                sender.sendMessage(ChatColor.BLUE + "Type: " + flag.getClass().getSimpleName());
                if (flag instanceof StateFlag) {
                    sender.sendMessage(ChatColor.BLUE + "Default: " + (((StateFlag) flag).getDefault() ? "ALLOW" : "DENY"));
                } else if (flag instanceof EnumFlag) {
                    Class enumClass = (Class) ClassHacker.getPrivateValue(flag, "enumClass");
                    if (enumClass != null) {
                        Object[] enumValues = enumClass.getEnumConstants();
                        String values = enumValues[0].toString();
                        for (int i = 1; i < enumValues.length; i++) {
                            values += "," + enumValues[i];
                        }
                        sender.sendMessage(ChatColor.BLUE + "Values: " + values);
                    }
                } else if (flag instanceof SetFlag
                        || flag instanceof CustomSetFlag) {
                    Flag<?> subFlag = (Flag<?>) ClassHacker.getPrivateValue(flag, "subFlag");
                    sender.sendMessage(ChatColor.BLUE + "Sub Flag Type: " + subFlag.getClass().getSimpleName());
                    if (subFlag instanceof SetFlag
                            || subFlag instanceof CustomSetFlag) {
                        Class enumClass = (Class) ClassHacker.getPrivateValue(subFlag, "enumClass");
                        if (enumClass != null) {
                            Object[] enumValues = enumClass.getEnumConstants();
                            String values = enumValues[0].toString();
                            for (int i = 1; i < enumValues.length; i++) {
                                values += "," + enumValues[i];
                            }
                            sender.sendMessage(ChatColor.BLUE + "Values: " + values);
                        }
                    }
                }
                String desc = FlagManager.getFlagDescription(flag.getName());
                if (desc != null) {
                    sender.sendMessage(ChatColor.BLUE + "Description: " + desc);
                }
                return true;
            } else {
                return false;
            }
        }

    }

    private void sendFlagList(CommandSender sender, boolean defaults)
    {
        ArrayList<String> flags = new ArrayList<String>();
        Set<String> keys = FlagManager.customFlags.keySet();
        if (!defaults)
        {
            flags.addAll(keys);
        }
        else
        {
            for (Flag<?> flag : DefaultFlag.getFlags())
            {
                flags.add(flag.getName());
            }
        }
        
        String[] names = flags.toArray(new String[0]);
        Arrays.sort(names);
        String text = (keys.contains(names[0]) ? ChatColor.AQUA : "") + names[0];
        for(int i = 1; i < names.length; i++)
        {
            text += ChatColor.GRAY + "," + (keys.contains(names[i]) ? ChatColor.AQUA : "") + names[i];
        }
        sender.sendMessage(text);
    }
    
}
