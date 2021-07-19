package com.birthdates.bperms.sponge.manager;

import com.birthdates.bperms.manager.PermissionManager;
import com.birthdates.bperms.permission.Permissible;
import com.birthdates.bperms.sponge.BPermsSponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.util.Tristate;

/**
 * {@inheritDoc}
 */
public class SpongePermissionManager extends PermissionManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void unsetPermissions(Object obj, Permissible permissible) {
        setPermission(obj, permissible, Tristate.FALSE);
    }

    /**
     * Set permissions from {@link Permissible} to a certain {@link Tristate}
     *
     * @param obj         Target player
     * @param permissible Target permissible
     * @param tristate    Target tristate
     */
    private void setPermission(Object obj, Permissible permissible, Tristate tristate) {
        BPermsSponge.validatePlayer(obj);
        Player player = (Player) obj;
        for (String permission : permissible.getAllPermissions()) {
            player.getSubjectData().setPermission(SubjectData.GLOBAL_CONTEXT, permission, tristate);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void givePermissions(Object obj, Permissible permissible) {
        setPermission(obj, permissible, Tristate.TRUE);
    }
}
