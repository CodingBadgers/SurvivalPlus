package uk.codingbadgers.commands;

import uk.codingbadgers.SurvivalPlus.module.Module;

public class CommandsTest extends Module {
    @Override
    public void onEnable() {
        registerCommands(TestCommand.class);
    }

    @Override
    public void onDisable() {

    }
}
