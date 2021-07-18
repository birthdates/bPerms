package com.birthdates.bperms.hook.type;

import com.birthdates.bperms.data.Profile;
import com.birthdates.bperms.data.Rank;

import java.util.UUID;

/**
 * Hook for player, profile & rank
 */
public class PlayerProfileRankHook extends PlayerProfileHook {

    /**
     * Our rank
     */
    private final Rank rank;

    public PlayerProfileRankHook(Rank rank, Profile profile, UUID uniqueId, Object player) {
        super(profile, uniqueId, player);
        this.rank = rank;
    }

    /**
     * Get the rank
     *
     * @return A {@link Rank}
     */
    public Rank getRank() {
        return rank;
    }
}
