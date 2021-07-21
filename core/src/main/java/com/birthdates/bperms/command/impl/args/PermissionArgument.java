package com.birthdates.bperms.command.impl.args;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.command.ArgFunction;
import com.birthdates.bperms.command.PermissibleArgument;
import com.birthdates.bperms.permission.Permissible;

public interface PermissionArgument<T extends Permissible> extends PermissibleArgument<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    default ArgFunction getFunction(T permissible) {
        return ((player, label, args) -> {
            String permission = args[0].toLowerCase();
            String server = args.length > 1 ? args[1] : "all";
            boolean removed = permissible.removePermission(server, permission);
            if (!removed)
                permissible.addPermission(server, permission);
            permissible.saveAsync();
            BPerms.getInstance().getPlayerManager().sendMessage(player, (removed ? "&c" : "&a")  + "You have " +
                    (removed ? "removed" : "added") + " the permission " + (removed ? "from" : "to") + " " + permissible.getName() + " for " + server + " server(s)");
        });
    }

}
