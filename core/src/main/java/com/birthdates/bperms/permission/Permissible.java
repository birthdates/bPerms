package com.birthdates.bperms.permission;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.data.PermissionGroup;
import com.birthdates.redisdata.data.impl.RedisDocument;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Abstract permissible class
 */
public abstract class Permissible extends RedisDocument {

    /**
     * Our prefix/suffix
     */
    protected String prefix = "";
    protected String suffix = "";
    /**
     * Our cached permissions
     */
    protected transient Set<String> cachedPermissions = new HashSet<>();
    /**
     * Our permissions (server -> permissions)
     */
    private Map<String, Map<String, Long>> permissions = new HashMap<>();
    /**
     * Our permission group ids
     */
    private Set<String> permissionGroups = new HashSet<>();

    /**
     * Get our translated prefix
     *
     * @return A {@link String}
     */
    public String getPrefix() {
        return BPerms.getInstance().getPlayerManager().translate(prefix);
    }

    /**
     * Set our prefix
     *
     * @param prefix New prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Get our translated suffix
     *
     * @return A {@link String}
     */
    public String getSuffix() {
        return BPerms.getInstance().getPlayerManager().translate(suffix);
    }

    /**
     * Set our suffix
     *
     * @param suffix New suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Save this document asynchronously
     */
    public void saveAsync() {
        BPerms.async(this::save);
    }

    private void removeExpiredPermissions() {
        if (permissions == null)
            return;
        for (Map.Entry<String, Map<String, Long>> entry : permissions.entrySet()) {
            for (Map.Entry<String, Long> permissionEntry : entry.getValue().entrySet()) {
                if (permissionEntry.getValue() > System.currentTimeMillis())
                    continue;
                entry.getValue().remove(permissionEntry.getKey());
            }
        }
    }

    public abstract String getName();

    /**
     * Fill {@link Permissible#cachedPermissions} with all our permissions
     */
    protected void fillCachedPermissions() {
        cachedPermissions = new HashSet<>();
        removeExpiredPermissions();

        if (permissions == null)
            permissions = new HashMap<>();
        if (permissionGroups == null)
            permissionGroups = new HashSet<>();

        // Fill
        if (BPerms.getInstance().getConfiguration().isBypassServerBasedPermissions()) {
            for (Map<String, Long> permissions : permissions.values()) {
                cachedPermissions.addAll(permissions.keySet());
            }
        } else {
            cachedPermissions.addAll(permissions.getOrDefault("all", Collections.emptyMap()).keySet());
            cachedPermissions.addAll(permissions.getOrDefault(BPerms.getInstance().getConfiguration().getServerId(), Collections.emptyMap()).keySet());
        }

        List<String> toRemove = new ArrayList<>();
        for (String groupId : permissionGroups) {
            PermissionGroup group = BPerms.getInstance().getPermissionManager().getPermissionGroupManager().getPermissionGroupById(groupId);
            if (group == null) {
                toRemove.add(groupId);
                continue;
            }
            cachedPermissions.addAll(group.getPermissions());
        }
        toRemove.forEach(permissionGroups::remove);
    }

    public void resetPermissionCache() {
        cachedPermissions = null;
    }

    /**
     * Get or create a set of permissions from a server
     *
     * @param server Target server
     * @return A not-null {@link Set} of {@link String}
     */
    @NotNull
    private Map<String, Long> getOrCreateServer(String server) {
        Map<String, Long> permissions = this.permissions.getOrDefault(server, null);
        if (permissions == null) {
            this.permissions.put(server, (permissions = new HashMap<>()));
        }
        return permissions;
    }

    /**
     * Add a permission to all servers
     *
     * @param permission Target permission
     */
    public void addPermission(String permission) {
        addPermission("all", permission);
    }

    public void addPermission(String server, String permission) {
        addPermission(server, permission, -1);
    }

    /**
     * Add a permission to a certain server (or all)
     *
     * @param server     Target server
     * @param permission Targer permission
     */
    public void addPermission(String server, String permission, long expiry) {
        getOrCreateServer(server).put(permission, expiry);
    }

    /**
     * Remove a permission from all servers
     *
     * @param permission Target permission
     * @return True, if this permission was found & removed. False, otherwise.
     */
    public boolean removePermission(String permission) {
        return removePermission("all", permission);
    }

    /**
     * Remove a permission from a certain server
     *
     * @param server     Target server
     * @param permission Target permission
     * @return True, if this permission was found & removed. False, otherwise.
     */
    public boolean removePermission(String server, String permission) {
        return getOrCreateServer(server).remove(permission) != null;
    }

    /**
     * Get all our permission groups
     *
     * @return A {@link Set} of {@link String} that are the permission groups' ids
     */
    public Set<String> getPermissionGroups() {
        return permissionGroups;
    }

    /**
     * Get all our permissions combined into one {@link Set}
     *
     * @return A {@link Set} of {@link String}
     */
    public Set<String> getAllPermissions() {
        if (cachedPermissions == null) {
            fillCachedPermissions();
        }
        return cachedPermissions;
    }
}
