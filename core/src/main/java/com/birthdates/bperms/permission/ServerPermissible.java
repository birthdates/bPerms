package com.birthdates.bperms.permission;

import com.birthdates.bperms.BPerms;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link Permissible} that only works on a certain server
 */
public abstract class ServerPermissible extends Permissible {

    /**
     * Servers that we apply to
     */
    private Set<String> servers = new HashSet<>();

    /**
     * Are we applicable to our server?
     *
     * @return True, if we are. False, otherwise.
     */
    public boolean isApplicable() {
        return servers.stream().anyMatch(server ->
                BPerms.getInstance().getConfiguration().isBypassServerBasedPermissions() ||
                server.equalsIgnoreCase("all") ||
                server.equalsIgnoreCase(BPerms.getInstance().getConfiguration().getServerId()));
    }

    /**
     * Get our servers
     *
     * @return A {@link Set} of {@link String}
     */
    public Set<String> getServers() {
        return servers;
    }
}
