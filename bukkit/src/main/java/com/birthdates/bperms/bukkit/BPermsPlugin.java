package com.birthdates.bperms.bukkit;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.bukkit.listener.PlayerListener;
import com.birthdates.bperms.vault.VaultHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin for Bukkit
 */
public class BPermsPlugin extends JavaPlugin {

    /**
     * Our instance
     */
    private static BPermsPlugin bPermsPlugin;

    /**
     * Get our instance
     *
     * @return A {@link BPermsPlugin}
     */
    public static BPermsPlugin getInstance() {
        return bPermsPlugin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoad() {
        bPermsPlugin = this;
        new BPermsBukkit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        BPermsBukkit.getInstance().enable();
        if (Bukkit.getPluginManager().isPluginEnabled("Vault"))
            VaultHook.init(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisable() {
        BPerms.getInstance().unload();
    }
}
