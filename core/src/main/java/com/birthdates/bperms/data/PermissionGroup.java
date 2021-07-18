package com.birthdates.bperms.data;

import com.birthdates.redisdata.data.impl.RedisDocument;

import java.util.HashSet;
import java.util.Set;

/**
 * Permission group document
 */
public class PermissionGroup extends RedisDocument {

    /**
     * Our ID
     */
    private final String id;

    /**
     * Our permissions
     */
    private Set<String> permissions = new HashSet<>();

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
     * Get all our permissions
     *
     * @return A {@link Set} of {@link String}
     */
    public Set<String> getPermissions() {
        return permissions;
    }
}
