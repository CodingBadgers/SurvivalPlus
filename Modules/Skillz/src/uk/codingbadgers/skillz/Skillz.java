package uk.codingbadgers.skillz;

import org.bukkit.event.Listener;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.skillz.commands.SkillCommand;

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

}
