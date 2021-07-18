package com.birthdates.bperms.manager;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.data.Profile;
import com.birthdates.bperms.permission.Permissible;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Abstract permission manager
 */
public abstract class PermissionManager {

    /**
     * Our permission group manager
     */
    private final PermissionGroupManager permissionGroupManager;

    public PermissionManager() {
        this.permissionGroupManager = new PermissionGroupManager();
    }

    /**
     * Give a player their permissions
     *
     * @param player Target player
     * @param uuid   Player's ID
     */
    public void givePermissions(Object player, UUID uuid) {
        Profile profile = BPerms.getInstance().getPlayerManager().getProfile(uuid, false);
        if (profile == null) {
            BPerms.getInstance().getPlayerManager().kickPlayer(player, "&cYour profile has not loaded yet.");
            return;
        }

        profile.setCachedPlayer(player);
        givePermissions(player, profile);
    }

    /**
     * Unset a player's permissions
     *
     * @param player      Target player
     * @param permissible Permissible to unset
     */
    public abstract void unsetPermissions(Object player, Permissible permissible);

    /**
     * Give a player permissions from a {@link Permissible}
     *
     * @param player      Target player
     * @param permissible Target permissible
     */
    public abstract void givePermissions(Object player, Permissible permissible);

    /**
     * Get our {@link PermissionGroupManager}
     *
     * @return A not-null {@link PermissionGroupManager}
     */
    @NotNull
    public PermissionGroupManager getPermissionGroupManager() {
        return permissionGroupManager;
    }
}
