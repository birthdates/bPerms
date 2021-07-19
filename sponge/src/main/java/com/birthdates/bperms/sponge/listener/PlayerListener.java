package com.birthdates.bperms.sponge.listener;

import com.birthdates.bperms.BPerms;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

/**
 * Listener for player join/leave
 */
public class PlayerListener {

    @Listener
    public void onAuth(ClientConnectionEvent.Auth event) {
        BPerms.getInstance().getPlayerManager().loadProfile(event.getProfile().getUniqueId());
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        BPerms.getInstance().getPermissionManager().givePermissions(event.getTargetEntity(), event.getTargetEntity().getUniqueId());
    }

    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect event) {
        BPerms.getInstance().getPlayerManager().unloadProfile(event.getTargetEntity().getUniqueId());
    }

}
