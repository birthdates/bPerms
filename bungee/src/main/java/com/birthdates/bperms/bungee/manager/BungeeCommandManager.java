package com.birthdates.bperms.bungee.manager;

import com.birthdates.bperms.bungee.BPermsPlugin;
import com.birthdates.bperms.command.Command;
import com.birthdates.bperms.manager.CommandManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

/**
 * {@inheritDoc}
 */
public class BungeeCommandManager extends CommandManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCommand(Command command) {
        ProxyServer.getInstance().getPluginManager().registerCommand(BPermsPlugin.getInstance(), new net.md_5.bungee.api.plugin.Command(command.getName(), command.getPermission(), command.getAliases()) {
            @Override
            public void execute(CommandSender commandSender, String[] args) {
                command.executed(commandSender, command.getName(), args);
            }
        });
    }

}
