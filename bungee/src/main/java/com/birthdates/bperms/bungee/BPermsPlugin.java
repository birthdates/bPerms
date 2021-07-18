package com.birthdates.bperms.bungee;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.bungee.listener.PlayerListener;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Plugin for BungeeCord
 */
public class BPermsPlugin extends Plugin {

    /**
     * Our instance
     */
    private static BPermsPlugin instance;

    /**
     * Get our instance
     *
     * @return A {@link BPermsPlugin}
     */
    public static BPermsPlugin getInstance() {
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoad() {
        instance = this;
        new BPermsBungee();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, new PlayerListener());
        BPermsBungee.getInstance().enable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisable() {
        BPerms.getInstance().unload();
    }
}
