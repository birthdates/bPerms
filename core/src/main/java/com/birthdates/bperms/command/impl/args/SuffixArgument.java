package com.birthdates.bperms.command.impl.args;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.command.ArgFunction;
import com.birthdates.bperms.command.PermissibleArgument;
import com.birthdates.bperms.permission.Permissible;

/**
 * {@link PermissibleArgument} for {@link Permissible#setSuffix(String)}
 *
 * @param <T> Target permissible type
 */
public interface SuffixArgument<T extends Permissible> extends PermissibleArgument<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    default ArgFunction getFunction(T permissible) {
        return ((player, label, args) -> {
            permissible.setSuffix(String.join(" ", args));
            permissible.saveAsync();
            BPerms.getInstance().getPlayerManager().sendMessage(player, "&aYou have set the prefix of " + permissible.getName() + " to "
                    + BPerms.getInstance().getPlayerManager().translate(permissible.getPrefix()));
        });
    }

}
