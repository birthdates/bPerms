package com.birthdates.bperms.sponge;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.manager.CommandManager;
import com.birthdates.bperms.manager.PermissionManager;
import com.birthdates.bperms.manager.PlayerManager;
import com.birthdates.bperms.sponge.manager.SpongeCommandManager;
import com.birthdates.bperms.sponge.manager.SpongePermissionManager;
import com.birthdates.bperms.sponge.manager.SpongePlayerManager;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;

/**
 * Sponge implementation for bPerms
 */
public class BPermsSponge extends BPerms {

    /**
     * Our managers
     */
    private final CommandManager commandManager = new SpongeCommandManager();
    private final PermissionManager permissionManager = new SpongePermissionManager();
    private final PlayerManager playerManager = new SpongePlayerManager();

    /**
     * Validate player is of type {@link Player}
     *
     * @param obj Target object
     */
    public static void validatePlayer(Object obj) {
        Validate.isTrue(obj instanceof Player, "Invalid type");
    }

    /**
     * Validate player is of type {@link CommandSource}
     *
     * @param obj Target object
     */
    public static void validateSender(Object obj) {
        Validate.isTrue(obj instanceof CommandSource, "Invalid type");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull File getConfigFile() {
        return new File(BPermsPlugin.getInstance().getConfigDir().toFile(), "config.toml");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveDefaultConfig() {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(getConfigFile());
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
            return;
        }
        saveDefaultConfig(inputStream);
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
        return "&e";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char getCommandPrefix() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logMessage(String message) {
        BPermsPlugin.getInstance().getLogger().log(Level.INFO, message);
    }
}
