package com.birthdates.bperms.bungee.listener;

import com.birthdates.bperms.BPerms;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Listener for player join/leave
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onPreJoin(LoginEvent event) {
        // This event is async
        BPerms.getInstance().getPlayerManager().loadProfile(event.getConnection().getUniqueId());
    }

    @EventHandler
    public void onPostJoin(PostLoginEvent event) {
        BPerms.getInstance().getPlayerManager().onJoin(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        BPerms.getInstance().getPlayerManager().onQuit(event.getPlayer());
    }
}
