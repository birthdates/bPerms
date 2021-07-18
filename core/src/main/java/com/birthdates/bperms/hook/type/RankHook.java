package com.birthdates.bperms.hook.type;

import com.birthdates.bperms.data.Rank;
import com.birthdates.bperms.hook.HookEvent;

/**
 * Rank hook
 */
public class RankHook extends HookEvent {

    /**
     * Our rank
     */
    private final Rank rank;

    public RankHook(Rank rank) {
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
