package com.birthdates.bperms.command;

import com.birthdates.bperms.command.impl.args.PermissionArgument;
import com.birthdates.bperms.command.impl.args.PermissionGroupArgument;
import com.birthdates.bperms.command.impl.args.PrefixArgument;
import com.birthdates.bperms.command.impl.args.SuffixArgument;
import com.birthdates.bperms.command.resolver.PermissibleResolver;
import com.birthdates.bperms.permission.Permissible;

/**
 * Argument commands that edit a {@link Permissible}
 *
 * @param <T> Type of {@link Permissible}
 */
public abstract class PermissibleArgumentCommand<T extends Permissible> extends ArgumentCommand {

    public PermissibleArgumentCommand(String name, String permission, int minArgs, String... aliases) {
        super(name, permission, minArgs, aliases);

        // Register base arguments
        registerArg((PrefixArgument<T>) this::getResolver, "<" + getPermissibleName() + "> <prefix> - Set a prefix of a " + getPermissibleName(), "prefix");
        registerArg((SuffixArgument<T>) this::getResolver, "<" + getPermissibleName() + "> <suffix> - Set a suffix of a " + getPermissibleName(), "suffix");
        registerArg((PermissionArgument<T>) this::getResolver, "<" + getPermissibleName() + "> <permission> (server|all) - Toggle a permission on a server", "permission", "perm", "p");
        registerArg((PermissionGroupArgument<T>) this::getResolver, "<" + getPermissibleName() + "> <permission group> (server|all) - Toggle a permission group on a server", "permissiongroup", "permgroup", "pgroup", "pg");
    }

    /**
     * Get the resolver for this argument (from type)
     *
     * @return A {@link PermissibleResolver} from type {@link T}
     */
    protected abstract PermissibleResolver<T> getResolver();

    /**
     * Get the name of this permissible (i.e rank, profile)
     *
     * @return A formatted lowercase {@link String} name
     */
    protected abstract String getPermissibleName();
}
