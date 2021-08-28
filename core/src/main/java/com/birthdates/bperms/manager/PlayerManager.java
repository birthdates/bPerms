package com.birthdates.bperms.manager;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.data.Profile;
import com.birthdates.bperms.hook.Hooks;
import com.birthdates.bperms.hook.type.PlayerHook;
import com.birthdates.bperms.hook.type.ProfileHook;
import com.birthdates.redisdata.data.RedisDataManager;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract player manager
 */
public abstract class PlayerManager extends RedisDataManager<Profile> {

    /**
     * Mojang API URL for name -> uuid conversion
     */
    private static final String NAME_TO_UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s";
    /**
     * UUID pattern to match from the URL above
     */
    private static final Pattern UUID_PATTERN = Pattern.compile("(?<=\"id\":\").*(?=\")");

    public PlayerManager() {
        startCheckingForExpiredRanks();
    }

    /**
     * Get a player's profile
     *
     * @param uuid Player's ID
     * @return A nullable {@link Profile}
     */
    @Nullable
    public Profile getProfile(UUID uuid) {
        return getProfile(uuid, true);
    }

    /**
     * Get a player's profile
     *
     * @param uuid    Player's ID
     * @param offline Load profile if offline?
     * @return A nullable {@link Profile}
     */
    public Profile getProfile(UUID uuid, boolean offline) {
        Profile profile = getData().getOrDefault(uuid.toString(), null);
        if (profile == null && offline) {
            profile = loadProfile(uuid);
        }
        return profile;
    }

    /**
     * Load a player's profile
     *
     * @param uuid Player's ID
     * @return A {@link Profile}
     */
    public Profile loadProfile(UUID uuid) {
        return loadProfile(uuid, false);
    }

    /**
     * Load a player's profile
     *
     * @param uuid    Player's ID
     * @param offline Is this load offline?
     * @return A {@link Profile}
     */
    public Profile loadProfile(UUID uuid, boolean offline) {
        if (!offline)
            BPerms.log("Loading permissions for: " + uuid.toString());
        dataLock.lock();
        Profile profile;
        try {
            profile = new Profile(uuid);
            profile.load();
            // Save new profiles
            if (!offline && profile.isNew())
                profile.save();
            if (!offline)
                addData(profile);
            BPerms.getInstance().getHookManager().callHook(Hooks.PROFILE_LOADED, new ProfileHook(profile));
            if (!offline)
                BPerms.log("Loaded permissions for: " + uuid);
        } finally {
            dataLock.unlock();
        }
        return profile;
    }

    /**
     * Call on player join
     *
     * @param player Target player
     */
    public void onJoin(Object player) {
        UUID id = getId(player);
        BPerms.getInstance().getPermissionManager().givePermissions(player, id);
        BPerms.getInstance().getHookManager().callHook(Hooks.PLAYER_JOINED, new PlayerHook(id, player));
    }

    /**
     * Call on player quit
     *
     * @param player Target player
     */
    public void onQuit(Object player) {
        Profile profile = unloadProfile(getId(player));
        if (profile == null)
            return;
        BPerms.getInstance().getPermissionManager().unsetPermissions(player, profile);
    }

    /**
     * Start checking each online profile for expired ranks
     */
    private void startCheckingForExpiredRanks() {
        BPerms.getInstance().getExecutor().scheduleAtFixedRate(this::checkForExpiredRanks, 10L, 10L, TimeUnit.SECONDS);
    }

    /**
     * Check each online profile for expired ranks
     */
    private void checkForExpiredRanks() {
        for (Map.Entry<String, Profile> entry : getData().entrySet()) {
            List<String> ranks = entry.getValue().removeExpiredRanks();
            if (ranks.isEmpty())
                continue;

            Object player = BPerms.getInstance().getPlayerManager().getPlayer(UUID.fromString(entry.getKey()));
            BPerms.getInstance().getPlayerManager().sendMessage(player, "&cSome of your temporary rank(s) have expired: " +
                    StringUtils.join(ranks, ", "));
        }
    }

    /**
     * Unload a player's profile
     *
     * @param uuid Player's ID
     * @return A {@link Profile}
     */
    public Profile unloadProfile(UUID uuid) {
        dataLock.lock();
        try {
            Profile profile = getProfile(uuid, false);
            if (profile == null)
                return null;

            BPerms.async(profile::save);
            removeData(uuid.toString());
            BPerms.getInstance().getHookManager().callHook(Hooks.PROFILE_UNLOADED, new ProfileHook(profile));
            BPerms.log("Unloaded permissions for: " + uuid);
            return profile;
        } finally {
            dataLock.unlock();
        }
    }

    /**
     * Send a translated message to a player
     *
     * @param player  Target player
     * @param message Target message
     */
    public abstract void sendMessage(Object player, String message);

    /**
     * Does this player have a permission?
     *
     * @param player     Target player
     * @param permission Target permission
     * @return True, if this player does have that permission. False, otherwise.
     */
    public abstract boolean hasPermission(Object player, String permission);

    /**
     * Kick this player with a translated reason
     *
     * @param player Target player
     * @param reason Target reason
     */
    public abstract void kickPlayer(Object player, String reason);

    /**
     * Get a player by their ID
     *
     * @param id Target id
     * @return A {@link Object} of a player
     */
    public abstract Object getPlayer(UUID id);

    /**
     * Get a player's ID
     *
     * @param player Target player
     * @return A {@link UUID}
     */
    public abstract UUID getId(Object player);

    /**
     * Fetch a request from {@link PlayerManager#NAME_TO_UUID_URL} that gets a player's UUID from their name
     *
     * @param name Player's name
     * @return The player's {@link UUID}
     */
    private UUID getOfflineId(String name) {
        String uri = String.format(NAME_TO_UUID_URL, name);
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            if (con.getResponseCode() != 200)
                throw new IOException("Received code that's not 200: " + con.getResponseCode());

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                String line = reader.readLine();
                Matcher matcher = UUID_PATTERN.matcher(line);
                if (!matcher.find()) throw new IOException("Failed to a UUID from: " + line);
                return UUID.fromString(matcher.group());
            }
        } catch (IOException | IllegalArgumentException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Get a {@link UUID} from a player's name
     *
     * @param name Target name
     * @return An offline {@link UUID}
     */
    public UUID getId(String name) {
        // First check if this name is a UUID
        try {
            return UUID.fromString(name);
        } catch (IllegalArgumentException exception) {
            // Check for online players with this name (saves API call)
            for (Object player : getOnlinePlayers()) {
                if (getName(player).equalsIgnoreCase(name)) return getId(player);
            }

            // Use Mojang API to fetch UUID from name
            return getOfflineId(name);
        }
    }

    /**
     * Translate the color codes in this message
     *
     * @param message Target message
     * @return A translated {@link String}
     */
    public abstract String translate(String message);

    /**
     * Get a offline player's name from their ID
     *
     * @param id Target ID
     * @return A {@link String} name or "Unknown" if not found
     */
    public abstract String getName(UUID id);

    /**
     * Get a player's name
     *
     * @param player Target player
     * @return A {@link String} name
     */
    public abstract String getName(Object player);

    /**
     * Get all online players
     *
     * @return A {@link Collection} of {@link Object} players
     */
    public abstract Collection<Object> getOnlinePlayers();

}
