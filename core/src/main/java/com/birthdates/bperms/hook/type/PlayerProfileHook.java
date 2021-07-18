package com.birthdates.bperms.hook.type;

import com.birthdates.bperms.data.Profile;

import java.util.UUID;

/**
 * Hook for player & profile
 */
public class PlayerProfileHook extends ProfileHook {

    /**
     * Player's ID
     */
    private final UUID uniqueId;
    /**
     * Player object
     */
    private final Object player;

    public PlayerProfileHook(Profile profile, UUID uniqueId, Object player) {
        super(profile);
        this.uniqueId = uniqueId;
        this.player = player;
    }

    /**
     * Get player's ID
     *
     * @return A {@link UUID}
     */
    public UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * Get the player
     *
     * @return A {@link Object}
     */
    public Object getPlayer() {
        return player;
    }
}
