package com.birthdates.bperms.permission;

import com.birthdates.bperms.BPerms;
import com.birthdates.redisdata.data.impl.RedisDocument;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    protected transient Set<String> cachedPermissions;
    /**
     * Our permissions (server -> permissions)
     */
    private Map<String, Map<String, Double>> permissions = new HashMap<>();
    /**
     * Our permission group ids
     */
    private Map<String, Map<String, Double>> permissionGroups = new HashMap<>();

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

    /**
     * Remove string from map if expired
     *
     * @param map Target map
     */
    private void removeExpired(Map<String, Map<String, Double>> map) {
        if (map == null)
            return;
        for (Map.Entry<String, Map<String, Double>> entry : map.entrySet()) {
            if (!entry.getValue().entrySet().removeIf(newEntry -> newEntry.getValue() > 0D && newEntry.getValue() <= System.currentTimeMillis()))
                continue;
            saveAsync();
        }
    }

    /**
     * Check for expired things
     */
    public void checkExpiry() {
        removeExpired(permissions);
        removeExpired(permissionGroups);
    }

    /**
     * Get our noun/name (i.e profile)
     *
     * @return A {@link String} noun
     */
    public abstract String getName();

    /**
     * Add to cached permissions
     *
     * @param map     Target server -> permissions map
     * @param mapFunc Map function of key set of permissions
     */
    private void addPermissions(Map<String, Map<String, Double>> map, Function<Collection<String>, Collection<String>> mapFunc) {
        // Fill
        if (BPerms.getInstance().getConfiguration().isBypassServerBasedPermissions()) {
            for (Map<String, Double> permissions : map.values()) {
                cachedPermissions.addAll(mapFunc.apply(permissions.keySet()));
            }
        } else {
            cachedPermissions.addAll(mapFunc.apply(map.getOrDefault("all", Collections.emptyMap()).keySet()));
            cachedPermissions.addAll(mapFunc.apply(map.getOrDefault(BPerms.getInstance().getConfiguration().getServerId(), Collections.emptyMap()).keySet()));
        }
    }


    /**
     * Fill {@link Permissible#cachedPermissions} with all our permissions
     */
    protected void fillCachedPermissions() {
        cachedPermissions = new HashSet<>();
        checkExpiry();

        if (permissions == null)
            permissions = new HashMap<>();
        if (permissionGroups == null)
            permissionGroups = new HashMap<>();

        // Fill
        addPermissions(permissions, perms -> perms);
        addPermissions(permissionGroups, perms -> {
            List<String> output = new ArrayList<>();
            Set<Set<String>> sets = perms.stream().map(perm -> BPerms.getInstance().getPermissionManager().getPermissionGroupManager().getPermissionGroupById(perm)).filter(Objects::nonNull).map(Permissible::getAllPermissions).collect(Collectors.toSet());
            for (Set<String> set : sets) {
                output.addAll(set);
            }
            return output;
        });
    }

    /**
     * Reset our permission cache
     */
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
    private Map<String, Double> getOrCreateServer(String server, Map<String, Map<String, Double>> target) {
        Map<String, Double> permissions = target.getOrDefault(server, null);
        if (permissions == null)
            target.put(server, (permissions = new HashMap<>()));
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

    /**
     * Add a permission to a servers
     *
     * @param server     Target server
     * @param permission Target permission
     */
    public void addPermission(String server, String permission) {
        addPermission(server, permission, -1L);
    }

    /**
     * Add a permission to a certain server (or all)
     *
     * @param server     Target server
     * @param permission Target permission
     * @param expiry     Target expiry (milliseconds)
     */
    public void addPermission(String server, String permission, long expiry) {
        getOrCreateServer(server, this.permissions).put(permission, expiry > 0D ? (double) (System.currentTimeMillis() + expiry) : expiry);
        resetPermissionCache();
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
        if (tryRemoveAll(server, permission, permissions)) {
            resetPermissionCache();
            return true;
        }
        boolean ret = getOrCreateServer(server, permissions).remove(permission) != null;
        if (ret)
            resetPermissionCache();
        return ret;
    }

    /**
     * Try remove permission if server is "all"
     *
     * @param server Target server
     * @param target Target permission
     * @param map    Target map
     * @return True if this operation was successful. False, otherwise.
     */
    private boolean tryRemoveAll(String server, String target, Map<String, Map<String, Double>> map) {
        if (!server.equalsIgnoreCase("all")) {
            return false;
        }
        boolean ret = false;
        for (Map.Entry<String, Map<String, Double>> entry : map.entrySet()) {
            if (entry.getValue().remove(target) != null)
                ret = true;
        }
        return ret;
    }

    /**
     * Remove a permission group from all servers
     *
     * @param id Target group's id
     * @return True, if the operation was successful. False, otherwise.
     */
    public boolean removePermissionGroup(String id) {
        return removePermissionGroup(id, "all");
    }

    /**
     * Remove a permission group from a server
     *
     * @param id     Target group's id
     * @param server Target server
     * @return True, if the operation was successful. False, otherwise.
     */
    public boolean removePermissionGroup(String id, String server) {
        boolean ret = tryRemoveAll(server, id, permissionGroups) || permissionGroups.remove(id) != null;
        if (ret)
            resetPermissionCache();
        return ret;
    }

    /**
     * Add a permission group to all servers
     *
     * @param id Target group's id
     * @return True, if the operation was successful. False, otherwise.
     */
    public boolean addPermissionGroup(String id) {
        return addPermissionGroup(id, "all", -1L);
    }

    /**
     * Add a permission group to a server
     *
     * @param id     Target group's id
     * @param server Target server
     * @param expiry Target expiry (milliseconds, -1 for permanent)
     * @return True, if the operation was successful. False, otherwise.
     */
    public boolean addPermissionGroup(String id, String server, long expiry) {
        Map<String, Double> groups = getOrCreateServer(server, permissionGroups);
        boolean ret = groups.putIfAbsent(id, expiry > 0L ? (double) (System.currentTimeMillis() + expiry) : -1.0D) == null;
        if (ret)
            resetPermissionCache();
        return ret;
    }

    /**
     * Get all our permissions combined into one {@link Set}
     *
     * @return A {@link Set} of {@link String}
     */
    public Set<String> getAllPermissions() {
        if (cachedPermissions == null)
            fillCachedPermissions();
        return Collections.unmodifiableSet(cachedPermissions);
    }
}
