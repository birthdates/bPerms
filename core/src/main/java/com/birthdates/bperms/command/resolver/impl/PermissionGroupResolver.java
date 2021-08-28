package com.birthdates.bperms.command.resolver.impl;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.command.resolver.PermissibleResolver;
import com.birthdates.bperms.data.PermissionGroup;

/**
 * {@link PermissibleResolver} of {@link PermissionGroup}
 */
public class PermissionGroupResolver implements PermissibleResolver<PermissionGroup> {

    /**
     * {@inheritDoc}
     */
    @Override
    public PermissionGroup getPermissible(String arg) {
        return BPerms.getInstance().getPermissionManager().getPermissionGroupManager().getPermissionGroupById(arg);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUnknownMessage() {
        return "&cA permission group with that name could not be found.";
    }
}
