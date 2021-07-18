package com.birthdates.bperms.bungee.manager;

import com.birthdates.bperms.bungee.BPermsBungee;
import com.birthdates.bperms.manager.PermissionManager;
import com.birthdates.bperms.permission.Permissible;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * {@inheritDoc}
 */
public class BungeePermissionManager extends PermissionManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsetPermissions(Object obj, Permissible permissible) {
        BPermsBungee.validatePlayer(obj);
        ProxiedPlayer player = (ProxiedPlayer) obj;

        for (String allPermission : permissible.getAllPermissions()) {
            player.setPermission(allPermission, false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void givePermissions(Object obj, Permissible permissible) {
        BPermsBungee.validatePlayer(obj);
        ProxiedPlayer player = (ProxiedPlayer) obj;

        for (String allPermission : permissible.getAllPermissions()) {
            player.setPermission(allPermission, true);
        }
    }

}
