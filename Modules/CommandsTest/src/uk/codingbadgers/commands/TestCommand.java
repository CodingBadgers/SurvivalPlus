package uk.codingbadgers.commands;

import org.bukkit.command.CommandSender;
import uk.codingbadgers.SurvivalPlus.commands.utils.Completions;
import uk.codingbadgers.SurvivalPlus.commands.annotations.*;
import uk.codingbadgers.SurvivalPlus.commands.utils.CommandArguments;
import uk.codingbadgers.SurvivalPlus.commands.utils.SenderType;
import uk.codingbadgers.SurvivalPlus.commands.exception.CommandUsageException;
import uk.codingbadgers.SurvivalPlus.module.Module;

public class TestCommand {

    @Command(value = "test", usage = "/<command> <info|test> [args]", description = "description")
    @Permission(value = "survivalplus.commands.test", message = "You cannot use this command dammit.")
    @Sender(SenderType.PLAYER)
    @Arguments(min = 0, max = 0)
    public void testCommand(CommandSender sender, CommandArguments args) {
        throw new CommandUsageException();
    }

    @Command(value = "test", usage = "/<command> test", description = "description")
    @Permission("survivalplus.commands.test.test")
    @Parent("test")
    @Sender(SenderType.PLAYER)
    @Arguments(min = 1, max = 1)
    public void childTestCommand(CommandSender sender, CommandArguments args) {
        Module.sendMessage("[Test]", sender, "This is a test message, have fun");
    }

    @Command(value = "info", usage = "/<command> info", description = "description")
    @Permission("survivalplus.commands.test.info")
    @Parent("test")
    @Sender(SenderType.PLAYER)
    @Arguments(min = 0, max = 1)
    public void childInfoCommand(CommandSender sender, CommandArguments args) {
        Module.sendMessage("[Test]", sender, "Here have some info for your troubles");
    }

    @TabCompletion("test.test")
    public Completions childTestTabCompleter(CommandSender sender, CommandArguments args) {
        Completions completions = new Completions();
        completions.add("test1");
        completions.add("test2");
        completions.add("test3");
        completions.add("test4");
        completions.add("test5");
        return completions;
    }

    @TabCompletion("test.info")
    public Completions childInfoTabCompleter(CommandSender sender, CommandArguments args) {
        Completions completions = new Completions();
        completions.add("info1");
        completions.add("info2");
        completions.add("info3");
        completions.add("info4");
        completions.add("info5");
        return completions;
    }
}
