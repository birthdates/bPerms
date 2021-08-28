package com.birthdates.bperms.bukkit.manager;

import com.birthdates.bperms.bukkit.BPermsBukkit;
import com.birthdates.bperms.bukkit.BPermsPlugin;
import com.birthdates.bperms.manager.PermissionManager;
import com.birthdates.bperms.permission.Permissible;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.ArrayList;
import java.util.Collection;

/**
 * {@inheritDoc}
 */
public class BukkitPermissionManager extends PermissionManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsetPermissions(Object object, Permissible permissible) {
        BPermsBukkit.validatePlayer(object);
        Player player = (Player) object;
        removeAllAttachments(player);
    }

    /**
     * Remove all {@link PermissionAttachment} from a player
     *
     * @param player Target player
     */
    private void removeAllAttachments(Player player) {
        Collection<PermissionAttachment> removedAttachments = new ArrayList<>();
        for (PermissionAttachmentInfo effectivePermission : player.getEffectivePermissions()) {
            if (effectivePermission.getAttachment() == null || !effectivePermission.getAttachment().getPlugin().equals(BPermsPlugin.getInstance()) || !removedAttachments.add(effectivePermission.getAttachment()))
                continue;

            player.removeAttachment(effectivePermission.getAttachment());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void givePermissions(Object object, Permissible permissible) {
        BPermsBukkit.validatePlayer(object);
        Player player = (Player) object;
        removeAllAttachments(player);
        PermissionAttachment attachment = player.addAttachment(BPermsPlugin.getInstance());
        for (String permission : permissible.getAllPermissions()) {
            attachment.setPermission(permission, true);
        }

        player.recalculatePermissions();
    }

}
