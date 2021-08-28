package com.birthdates.bperms.command.impl.permissiongroup;

import com.birthdates.bperms.command.PermissibleArgumentCommand;
import com.birthdates.bperms.command.resolver.PermissibleResolver;
import com.birthdates.bperms.command.resolver.impl.PermissionGroupResolver;
import com.birthdates.bperms.data.PermissionGroup;

/**
 * A command for editing a {@link PermissionGroup}
 */
public class PermissionGroupCommand extends PermissibleArgumentCommand<PermissionGroup> {

    /**
     * Our resolver
     */
    private static final PermissibleResolver<PermissionGroup> RESOLVER = new PermissionGroupResolver();

    public PermissionGroupCommand() {
        super("permissiongroup", "bperms.permissiongroup", 1, "pgroup", "pg");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PermissibleResolver<PermissionGroup> getResolver() {
        return RESOLVER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPermissibleName() {
        return "permission group";
    }
}
