package com.birthdates.bperms.bukkit.manager;

import com.birthdates.bperms.command.Command;
import com.birthdates.bperms.manager.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * {@inheritDoc}
 */
public class BukkitCommandManager extends CommandManager {

    /**
     * Bukkit command map
     */
    private final CommandMap commandMap;

    public BukkitCommandManager() {
        // Try to find command map
        CommandMap value;
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            value = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
            value = null;
        }
        commandMap = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCommand(Command command) {
        if (commandMap == null)
            return;

        commandMap.register(command.getName(), new org.bukkit.command.Command(command.getName(), "", command.getUsageArgs(), Arrays.asList(command.getAliases())) {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String label, @NotNull String[] args) {
                command.executed(commandSender, label, args);
                return true;
            }
        });
    }
}
