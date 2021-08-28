package com.birthdates.bperms.command.impl.args;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.command.ArgFunction;
import com.birthdates.bperms.command.PermissibleArgument;
import com.birthdates.bperms.permission.Permissible;
import com.birthdates.bperms.utils.TimeUtil;

/**
 * {@link PermissibleArgument} for permission groups
 *
 * @param <T> Target permissible type
 */
public interface PermissionGroupArgument<T extends Permissible> extends PermissibleArgument<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    default ArgFunction getFunction(T permissible) {
        return ((player, label, args) -> {
            String id = args[0].toLowerCase();
            String server = args.length > 1 ? args[1] : "all";
            if (BPerms.getInstance().getPermissionManager().getPermissionGroupManager().getPermissionGroupById(id) == null) {
                BPerms.getInstance().getPlayerManager().sendMessage(player, "&cInvalid permission group.");
                return;
            }
            boolean removed = permissible.removePermissionGroup(id, server);
            if (!removed)
                permissible.addPermissionGroup(id, server, args.length > 2 ? TimeUtil.getTime(args[2]) : -1L);
            permissible.saveAsync();
            BPerms.getInstance().getPlayerManager().sendMessage(player, (removed ? "&c" : "&a") + "You have " +
                    (removed ? "removed" : "added") + " the permission group \"" + id + "\" " + (removed ? "from" : "to") + " " + permissible.getName());
        });
    }

}
