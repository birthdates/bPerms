package com.birthdates.bperms.bukkit;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.bukkit.manager.BukkitCommandManager;
import com.birthdates.bperms.bukkit.manager.BukkitPermissionManager;
import com.birthdates.bperms.bukkit.manager.BukkitPlayerManager;
import com.birthdates.bperms.manager.CommandManager;
import com.birthdates.bperms.manager.PermissionManager;
import com.birthdates.bperms.manager.PlayerManager;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Level;

/**
 * Bukkit instance for {@link BPerms}
 */
public class BPermsBukkit extends BPerms {

    /**
     * Our managers
     */
    private static final String CONFIG_FILE = "config.toml";
    private final PermissionManager permissionManager = new BukkitPermissionManager();
    private final PlayerManager playerManager = new BukkitPlayerManager();
    private final CommandManager commandManager = new BukkitCommandManager();

    /**
     * Validate player is of type {@link Player}
     *
     * @param player Target object
     */
    public static void validatePlayer(Object player) {
        Validate.isTrue(player instanceof Player, "Invalid player.");
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
    @NotNull
    public File getConfigFile() {
        return new File(BPermsPlugin.getInstance().getDataFolder(), CONFIG_FILE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveDefaultConfig() {
        saveDefaultConfig(BPermsPlugin.getInstance().getResource(CONFIG_FILE));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logMessage(String message) {
        BPermsPlugin.getInstance().getLogger().log(Level.INFO, message);
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
        return "&b";
    }
}
