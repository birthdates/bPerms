package com.birthdates.bperms.hook.type;

import com.birthdates.bperms.hook.HookEvent;

import java.util.UUID;

/**
 * Hook for players
 */
public class PlayerHook extends HookEvent {

    /**
     * Player's ID
     */
    private final UUID uniqueId;
    /**
     * Player object
     */
    private final Object player;

    public PlayerHook(UUID uniqueId, Object player) {
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
