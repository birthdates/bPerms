package com.birthdates.bperms.bukkit.listener;

import com.birthdates.bperms.BPerms;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener for player join/leave
 */
public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        BPerms.getInstance().getPlayerManager().loadProfile(event.getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        BPerms.getInstance().getPlayerManager().onJoin(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        BPerms.getInstance().getPlayerManager().onQuit(event.getPlayer());
    }
}
