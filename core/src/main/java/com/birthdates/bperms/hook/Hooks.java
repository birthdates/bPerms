package com.birthdates.bperms.hook;

import com.birthdates.bperms.hook.type.PlayerHook;
import com.birthdates.bperms.hook.type.PlayerProfileRankHook;
import com.birthdates.bperms.hook.type.ProfileHook;
import com.birthdates.bperms.hook.type.RankHook;

/**
 * All our hooks
 */
public enum Hooks {
    PROFILE_LOADED("profile_loaded", ProfileHook.class),
    PROFILE_UNLOADED("profile_unloaded", ProfileHook.class),
    RANK_CREATED("rank_created", RankHook.class),
    RANK_REMOVED("rank_removed", RankHook.class),
    RANK_LOADED("rank_loaded", RankHook.class),
    RANK_CHANGED("rank_changed", RankHook.class),
    RANK_CHANGED_PERMISSIONS("rank_changed_permissions", RankHook.class),
    PLAYER_GRANTED_RANK("player_granted_rank", PlayerProfileRankHook.class),
    PLAYER_REVOKED_RANK("player_revoked_rank", PlayerProfileRankHook.class),
    PLAYER_JOINED("player_joined", PlayerHook.class);

    /**
     * Hook ID
     */
    private final String id;
    /**
     * Hook event type
     */
    private final Class<? extends HookEvent> type;

    Hooks(String id, Class<? extends HookEvent> type) {
        this.id = id;
        this.type = type;
    }

    /**
     * Get our hook's ID
     *
     * @return A unique {@link String} ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get our hook's event type
     *
     * @return A {@link Class} that extends {@link HookEvent}
     */
    public Class<? extends HookEvent> getType() {
        return type;
    }
}
