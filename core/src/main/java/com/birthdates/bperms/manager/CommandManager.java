package com.birthdates.bperms.manager;

import com.birthdates.bperms.command.Command;

/**
 * Our command manager
 */
public abstract class CommandManager {

    /**
     * Abstractly register a command
     *
     * @param command Target command
     */
    public abstract void registerCommand(Command command);

    /**
     * Register multiple commands
     *
     * @param commands Array of commands
     */
    public final void registerCommands(Command... commands) {
        for (Command command : commands) {
            registerCommand(command);
        }
    }
}
