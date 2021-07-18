package com.birthdates.bperms.manager;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.data.Rank;
import com.birthdates.bperms.hook.Hooks;
import com.birthdates.bperms.hook.type.RankHook;
import com.birthdates.redisdata.data.RedisDataManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Rank manager
 */
public class RankManager extends RedisDataManager<Rank> {

    /**
     * The default rank
     */
    private Rank defaultRank;
    /**
     * Mapped & sorted ranks
     */
    private List<Rank> sortedRanks = new ArrayList<>();

    public RankManager() {
        try {
            loadAll("ranks", Rank.class.getConstructor(String.class));
        } catch (NoSuchMethodException exception) {
            exception.printStackTrace();
        }

        // Add default rank
        if (getData().isEmpty() || doesNotHaveDefaultRank()) {
            BPerms.log("No default rank. Adding a new default rank called \"default\".");
            Rank rank = (defaultRank = new Rank("default", "Default"));
            rank.setDefaultRank(true);
            rank.setColor("&7");
            rank.setPrefix("&7");
            rank.getServers().add("all");
            rank.save();
            addData(rank);
        }
        sortRanks();
    }

    /**
     * Sort our ranks
     */
    private void sortRanks() {
        sortedRanks = new ArrayList<>(getData().values());
        sortedRanks.sort(Comparator.comparingDouble(Rank::getWeight));
    }

    /**
     * Do we have a default rank on this server?
     *
     * @return True, if we do. False, otherwise.
     */
    private boolean doesNotHaveDefaultRank() {
        for (Rank rank : getData().values()) {
            if (!rank.isDefaultRank() || !rank.isApplicable())
                continue;

            defaultRank = rank;
            return false;
        }
        return true;
    }

    /**
     * Remove a rank
     *
     * @param rank Target rank
     */
    public void removeRank(Rank rank) {
        BPerms.async(rank::delete);
        removeData(rank.getId(), false);
        sortRanks();
        BPerms.getInstance().getHookManager().callHook(Hooks.RANK_REMOVED, new RankHook(rank));
    }

    /**
     * Add a rank
     *
     * @param rank Target rank
     */
    public void addRank(Rank rank) {
        addData(rank);
        sortRanks();
        BPerms.getInstance().getHookManager().callHook(Hooks.RANK_CREATED, new RankHook(rank));
    }

    /**
     * Remove a rank by it's ID
     *
     * @param id Target ID
     * @return True, if this rank was removed. False, otherwise.
     */
    public boolean removeRank(String id) {
        for (Map.Entry<String, Rank> entry : getData().entrySet()) {
            if (!entry.getKey().equalsIgnoreCase(id) && !entry.getValue().getName().equalsIgnoreCase(id))
                continue;

            removeRank(entry.getValue());
            return true;
        }

        return false;
    }

    /**
     * Get the default rank.
     *
     * @return A not-null {@link Rank}
     */
    @NotNull
    public Rank getDefaultRank() {
        return defaultRank;
    }

    /**
     * Get a sorted list of ranks (by weight)
     *
     * @return A sorted {@link List} of {@link Rank}
     */
    public List<Rank> getSortedRanks() {
        return sortedRanks;
    }

    /**
     * Get a rank by it's ID
     *
     * @param id Target ID
     * @return A {@link Rank} if found. Null, otherwise.
     */
    @Nullable
    public Rank getRankById(String id) {
        return getData().getOrDefault(id, null);
    }
}
