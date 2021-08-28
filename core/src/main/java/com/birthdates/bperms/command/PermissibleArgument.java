package com.birthdates.bperms.command;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.command.resolver.PermissibleResolver;
import com.birthdates.bperms.permission.Permissible;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Argument that gives permissible of type T
 *
 * @param <T> Target type
 */
public interface PermissibleArgument<T extends Permissible> extends ArgFunction {

    /**
     * Callback with permissible
     *
     * @param permissible Target permissible
     * @return An {@link ArgFunction} for the argument
     */
    ArgFunction getFunction(T permissible);

    /**
     * Get our {@link PermissibleResolver}
     *
     * @return A {@link PermissibleResolver} of type T
     */
    PermissibleResolver<T> getPermissibleFinder();

    /**
     * {@inheritDoc}
     */
    @Override
    default void executed(Object player, String label, String[] args) {
        if (args.length < 2) {
            BPerms.getInstance().getPlayerManager().sendMessage(player, "&cInvalid arguments!");
            return;
        }
        T permissible = getPermissibleFinder().getPermissible(args[0]);
        if (permissible == null) {
            BPerms.getInstance().getPlayerManager().sendMessage(player, getPermissibleFinder().getUnknownMessage());
            return;
        }
        getFunction(permissible).executed(player, label, ArrayUtils.remove(args, 0));
    }
}
