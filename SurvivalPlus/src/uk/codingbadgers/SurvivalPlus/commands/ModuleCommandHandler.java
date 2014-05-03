/**
 * bFundamentals 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
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
package uk.codingbadgers.SurvivalPlus.commands;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;

import com.google.common.collect.ImmutableList;

import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.module.Module;

/**
 * The Module Command handler, handles internal command registering.
 * <p>
 * SHOULD NOT BE USED OUTSIDE bFundamentals
 */
public class ModuleCommandHandler {

	// TODO check each update to make sure they don't change the field name
	private static final String commandMapFieldName = "commandMap";
	private static final String commandsFieldName = "knownCommands";
	
	private static Map<Module, List<ModuleCommand>> commands = new HashMap<Module, List<ModuleCommand>>();
	private static CommandMap commandMap;
	private static Field knownCommandsField;
	
	static {
		setupCommandMap();
	}

	private static void setupCommandMap() {
		// reflection to make sure we don't have to use OBC code
		try {
			Class<? extends PluginManager> clazz = Bukkit.getServer().getPluginManager().getClass();
			Field field = clazz.getDeclaredField(commandMapFieldName);
			field.setAccessible(true);
			commandMap = (CommandMap) field.get(Bukkit.getServer().getPluginManager());
		} catch (Exception e) {
			SurvivalPlus.log(Level.INFO, "Error setting up command handler", e);
			commandMap = null;
		}
		
		if (commandMap instanceof SimpleCommandMap) {
			try {
				knownCommandsField = SimpleCommandMap.class.getDeclaredField(commandsFieldName);
				knownCommandsField.setAccessible(true);
			} catch (Exception e) {
				SurvivalPlus.log(Level.INFO, "Error setting up command handler", e);
			}
		} else {
			SurvivalPlus.log(Level.SEVERE, "Unknown command map type, cannot deregister commands.");
		}

	}
	
	/**
	 * Register command.
	 *
	 * @param module the module
	 * @param command the command
	 */
	public static void registerCommand(Module module, ModuleCommand command) {
		command.register(module);
		commandMap.register(module.getName(), command);
		
		if (commands.containsKey(module)) {
			commands.get(module).add(command);
		} else {
			commands.put(module, new ArrayList<ModuleCommand>(Arrays.asList(command)));
		}
	}

	/**
	 * Deregister all commands from a module.
	 *
	 * @param module the module
	 */
	@SuppressWarnings("unchecked")
	public static void deregisterCommand(Module module) {

		if (!commands.containsKey(module) || getCommands(module) == null) {
			return;
		}
		
		List<ModuleCommand> commands = new ArrayList<ModuleCommand>(getCommands(module));
		for (ModuleCommand command : commands) {
			ModuleCommandHandler.commands.get(module).remove(command);

			try {
				Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
				Command theCommand = commandMap.getCommand(command.getLabel());
				
				if (theCommand == null) {
				    throw new Exception("Cannot find command " + command.getLabel() + " in bukkit command map");
				}
				
				theCommand.unregister(commandMap);
				knownCommands.remove(theCommand.getLabel().toLowerCase());
				if (SurvivalPlus.getConfigurationManager().isDebugEnabled()) {
					SurvivalPlus.log(Level.INFO, theCommand.getLabel().toLowerCase() + " for module " + module.getName() + " has been deregistered successfully");
				}
			} catch (Throwable e) {
				SurvivalPlus.log(Level.INFO, "Error deregistrying " + command.getName() + " for module " + module.getName(), e);
			}
		}
	}

	/**
	 * Gets the commands for a module.
	 *
	 * @param module the module
	 * @return the commands for that module
	 */
	public static List<ModuleCommand> getCommands(Module module) {
	    if (!commands.containsKey(module)) {
	        return new ImmutableList.Builder<ModuleCommand>().build();
	    }
		return new ImmutableList.Builder<ModuleCommand>().addAll(commands.get(module)).build();
	}	
}
