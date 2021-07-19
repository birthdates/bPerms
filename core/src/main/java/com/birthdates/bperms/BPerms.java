package com.birthdates.bperms;

import com.birthdates.bperms.command.impl.RankCommand;
import com.birthdates.bperms.config.Configuration;
import com.birthdates.bperms.manager.CommandManager;
import com.birthdates.bperms.manager.HookManager;
import com.birthdates.bperms.manager.PermissionManager;
import com.birthdates.bperms.manager.PlayerManager;
import com.birthdates.bperms.manager.RankManager;
import com.birthdates.redisdata.RedisManager;
import com.moandjiezana.toml.Toml;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Abstract API instance
 */
public abstract class BPerms {

    /**
     * Our instance
     */
    private static BPerms instance;

    /**
     * Our toml parser instance
     */
    private final Toml toml = new Toml();

    /**
     * Our configuration
     */
    private final Configuration configuration;

    /**
     * Our rank manager
     */
    private final RankManager rankManager;

    /**
     * Our hook manager
     */
    private final HookManager hookManager;

    /**
     * Our executor
     */
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    /**
     * Our main constructor
     */
    public BPerms() {
        // Singleton stuff
        Validate.isTrue(instance == null, "bPerms already instantiated!");
        instance = this;

        // Register hook manager
        hookManager = new HookManager();

        // Try to make config file
        File configFile = getConfigFile();
        if (!configFile.exists()) {
            saveDefaultConfig();
            Validate.isTrue(configFile.exists(), "Save default config did not work.");
        }
        // Load configuration
        configuration = Configuration.load(toml.read(configFile));

        // Load Redis
        RedisManager.init(null, configuration.getRedis().getHost(), configuration.getRedis().getDatabase(), configuration.getRedis().getPort(), configuration.getRedis().getUsername(), configuration.getRedis().getPassword());

        // Load rank manager
        rankManager = new RankManager();
        log("Startup complete.");
    }

    /**
     * Get the {@link BPerms} instance
     *
     * @return A {@link BPerms}
     */
    @NotNull
    public static BPerms getInstance() {
        return instance;
    }

    /**
     * Utility for running task async
     *
     * @param task Target task
     */
    public static void async(Runnable task) {
        BPerms.getInstance().asyncTask(task);
    }

    /**
     * Utility for logging a message
     *
     * @param message Target message
     */
    public static void log(String message) {
        instance.logMessage(message);
    }

    /**
     * Get our {@link ScheduledExecutorService} (single-threaded)
     *
     * @return A {@link ScheduledExecutorService}
     */
    @NotNull
    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    /**
     * Called when we want to enable this instance
     */
    public void enable() {
        registerCommands();

        // Load all data for online players
        for (Object player : getPlayerManager().getOnlinePlayers()) {
            getPermissionManager().givePermissions(player, getPlayerManager().loadProfile(getPlayerManager().getId(player)));
        }
    }

    /**
     * Register our commands
     */
    public final void registerCommands() {
        Validate.isTrue(getCommandManager() != null, "No command manager.");
        getCommandManager().registerCommands(new RankCommand());
    }

    /**
     * Called on unload
     */
    public void unload() {
        getPlayerManager().saveAll();

        executor.shutdown();
        try {
            if (!executor.awaitTermination(3L, TimeUnit.SECONDS))
                executor.shutdownNow();
        } catch (InterruptedException ignored) {

        }

        instance = null;
    }

    /**
     * Get the {@link File} that represents our config toml file (i.e plugins/bPerms/config.toml)
     *
     * @return A {@link File}
     */
    @NotNull
    public abstract File getConfigFile();

    /**
     * Save our default config
     */
    public abstract void saveDefaultConfig();

    /**
     * Save our default config with an {@link InputStream}
     *
     * @param inputStream Target stream
     */
    public final void saveDefaultConfig(InputStream inputStream) {
        Validate.isTrue(inputStream != null, "Config file not found in JAR");
        File configFile = getConfigFile();
        try {
            if (!configFile.mkdirs() && configFile.createNewFile())
                throw new IllegalStateException("Failed to create config file.");
            Files.copy(inputStream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new IllegalArgumentException(exception);
        }
    }

    /**
     * Get our abstract permission manager
     *
     * @return A {@link PermissionManager}
     */
    public abstract PermissionManager getPermissionManager();

    /**
     * Get our abstract player manager
     *
     * @return A {@link PlayerManager}
     */
    public abstract PlayerManager getPlayerManager();

    /**
     * Get our abstract command manager
     *
     * @return A {@link CommandManager}
     */
    public abstract CommandManager getCommandManager();

    /**
     * Get our main color
     *
     * @return A {@link String} color like &b
     */
    public abstract String getColor();

    /**
     * Get the character prefix for commands (i.e if it's b, /rank -> /brank)
     *
     * @return A {@link Character}
     */
    public abstract char getCommandPrefix();

    /**
     * Run task async with executor
     *
     * @param task Target task
     */
    public final void asyncTask(Runnable task) {
        executor.submit(task);
    }

    /**
     * Log a message
     *
     * @param message Target message
     */
    public abstract void logMessage(String message);

    /**
     * Get our configuration
     *
     * @return A {@link Configuration}
     */
    @NotNull
    public final Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Get our rank manager
     *
     * @return A {@link RankManager}
     */
    public RankManager getRankManager() {
        return rankManager;
    }

    /**
     * Get our hook manager
     *
     * @return A {@link HookManager}
     */
    public HookManager getHookManager() {
        return hookManager;
    }
}
