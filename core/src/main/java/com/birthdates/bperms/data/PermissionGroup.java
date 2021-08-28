package com.birthdates.bperms.data;

import com.birthdates.bperms.permission.ServerPermissible;

/**
 * Permission group document
 */
public class PermissionGroup extends ServerPermissible {

    /**
     * Our ID
     */
    private final String id;

    public PermissionGroup(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespace() {
        return "permission_groups";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getType() {
        return PermissionGroup.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return id;
    }
}
