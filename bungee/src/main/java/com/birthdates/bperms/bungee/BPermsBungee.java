package com.birthdates.bperms.bungee;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.bungee.manager.BungeeCommandManager;
import com.birthdates.bperms.bungee.manager.BungeePermissionManager;
import com.birthdates.bperms.bungee.manager.BungeePlayerManager;
import com.birthdates.bperms.manager.CommandManager;
import com.birthdates.bperms.manager.PermissionManager;
import com.birthdates.bperms.manager.PlayerManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;

public class BPermsBungee extends BPerms {

    /**
     * Our managers
     */
    private final PermissionManager permissionManager = new BungeePermissionManager();
    private final CommandManager commandManager = new BungeeCommandManager();
    private final PlayerManager playerManager = new BungeePlayerManager();

    /**
     * Validate player is of type {@link ProxiedPlayer}
     *
     * @param player Target object
     */
    public static void validatePlayer(Object player) {
        Validate.isTrue(player instanceof ProxiedPlayer, "Invalid player.");
    }

    /**
     * Validate player is of type {@link CommandSender}
     *
     * @param sender Target object
     */
    public static void validateSender(Object sender) {
        Validate.isTrue(sender instanceof CommandSender, "Invalid sender.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull File getConfigFile() {
        return new File(BPermsPlugin.getInstance().getDataFolder(), "config.toml");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveDefaultConfig() {
        saveDefaultConfig(BPermsPlugin.getInstance().getResourceAsStream("config.toml"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColor() {
        return "&d";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logMessage(String message) {
        BPermsPlugin.getInstance().getLogger().log(Level.INFO, message);
    }
}
