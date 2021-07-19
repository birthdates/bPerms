package com.birthdates.bperms.sponge;

import com.birthdates.bperms.BPerms;
import com.google.inject.Inject;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Sponge plugin for bPerms
 */
@Plugin(id = "bperms", name = "bPerms", version = "1.0.0", description = "A permissions plugin")
public class BPermsPlugin {

    /**
     * Our instance
     */
    private static BPermsPlugin instance;
    /**
     * Plugin's logger
     */
    @Inject
    private Logger logger;
    /**
     * Our config's directory
     */
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;
    /**
     * Game instance
     */
    @Inject
    private Game game;

    public BPermsPlugin() {
        instance = this;
        new BPermsSponge();
    }

    /**
     * Get our plugin instance
     *
     * @return A {@link BPermsPlugin}
     */
    public static BPermsPlugin getInstance() {
        return instance;
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        BPerms.getInstance().enable();
    }

    /**
     * Get our config directory
     *
     * @return A {@link Path}
     */
    public Path getConfigDir() {
        return configDir;
    }

    /**
     * Get our plugin logger
     *
     * @return A {@link Logger}
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Get the game instance
     *
     * @return A {@link Game}
     */
    public Game getGame() {
        return game;
    }
}
