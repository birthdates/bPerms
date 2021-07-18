package com.birthdates.bperms.config;

import com.moandjiezana.toml.Toml;

import java.util.Objects;

/**
 * Our main configuration
 */
public class Configuration {

    /**
     * Server's ID
     */
    private String serverId;
    /**
     * Bypass server based limits?
     */
    private boolean bypassServerBasedPermissions;
    /**
     * Redis settings
     */
    private RedisSettings redis;

    public static Configuration load(Toml toml) {
        return toml.to(Configuration.class);
    }

    /**
     * Get our server id
     *
     * @return A {@link String} id
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * Get our redis settings
     *
     * @return A {@link RedisSettings}
     */
    public RedisSettings getRedis() {
        return redis;
    }

    /**
     * Are we bypassing server based permissions?
     *
     * @return True, if we are. False, otherwise.
     */
    public boolean isBypassServerBasedPermissions() {
        return bypassServerBasedPermissions;
    }

    /**
     * Redis settings
     */
    public static class RedisSettings {
        /**
         * Redis host, username, password
         */
        private final String host, username, password;
        /**
         * Redis port & database number
         */
        private final int port, database;

        public RedisSettings(String host, String username, String password, int port, int database) {
            this.host = host;
            this.username = username;
            this.password = password;
            this.port = port;
            this.database = database;
        }

        /**
         * Get the Redis host
         *
         * @return A {@link String}
         */
        public String getHost() {
            return host;
        }

        /**
         * Get the Redis username
         *
         * @return A {@link String}
         */
        public String getUsername() {
            return Objects.equals(username, "") ? null : username;
        }

        /**
         * Get the Redis password
         *
         * @return A {@link String}
         */
        public String getPassword() {
            return Objects.equals(password, "") ? null : password;
        }

        /**
         * Get the Redis port
         *
         * @return An {@link Integer}
         */
        public int getPort() {
            return port;
        }

        /**
         * Get the Redis database number
         *
         * @return An {@link Integer}
         */
        public int getDatabase() {
            return database;
        }
    }
}
