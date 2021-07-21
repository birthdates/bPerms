package com.birthdates.bperms.data;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.hook.Hooks;
import com.birthdates.bperms.hook.type.PlayerProfileRankHook;
import com.birthdates.bperms.permission.Permissible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Profile document
 */
public class Profile extends Permissible {

    /**
     * Our ID
     */
    private transient final UUID id;
    /**
     * Our cached ranks
     */
    private transient final Set<Rank> cachedRanks = new HashSet<>();
    /**
     * Our best rank (by weight)
     */
    private transient Rank bestRank;
    /**
     * Our cached player object
     */
    private transient Object cachedPlayer;
    /**
     * Our rank id -> expiry map (-1 = permanent)
     */
    private Map<String, Double> ranks = new HashMap<>();
    private String lastName;

    public Profile(UUID id) {
        this.id = id;
    }

    @Override
    public String getName() {
        lastName = cachedPlayer != null ? BPerms.getInstance().getPlayerManager().getName(cachedPlayer) : BPerms.getInstance().getPlayerManager().getName(id);
        return lastName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fillCachedPermissions() {
        super.fillCachedPermissions();
        if (bestRank == null)
            updateBestRank();
        cachedPermissions.addAll(bestRank.getAllPermissions());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoaded() {
        ranks.putIfAbsent(BPerms.getInstance().getRankManager().getDefaultRank().getId(), -1.0D);
        removeExpiredRanks();
        cachedRanks.addAll(ranks.keySet().stream()
                .map(rank -> BPerms.getInstance().getRankManager().getRankById(rank))
                .filter(rank -> rank != null && rank.isApplicable()).collect(Collectors.toSet()));
    }

    /**
     * Get our cached player
     *
     * @return A {@link Object}
     */
    @Nullable
    public Object getCachedPlayer() {
        return cachedPlayer;
    }

    /**
     * Set our cached player
     *
     * @param cachedPlayer New player
     */
    public void setCachedPlayer(Object cachedPlayer) {
        this.cachedPlayer = cachedPlayer;
    }

    /**
     * Get our player's id
     *
     * @return A {@link UUID}
     */
    public UUID getUniqueId() {
        return id;
    }

    /**
     * Remove all our expired ranks
     *
     * @return A {@link List} of {@link String} that we removed
     */
    public List<String> removeExpiredRanks() {
        List<String> list = new ArrayList<>();
        boolean ret = ranks.entrySet().removeIf(entry -> {
            if (entry.getValue() < 0.0D || entry.getValue() > System.currentTimeMillis())
                return false;
            ranks.remove(entry.getKey());
            list.add(BPerms.getInstance().getRankManager().getRankById(entry.getKey()).getName());
            return true;
        });;
        if (ret)
            saveAsync();
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespace() {
        return "bPerms:profiles";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getType() {
        return Profile.class;
    }

    /**
     * Update our cached best rank
     */
    private void updateBestRank() {
        double highestWeight = -1.0D;
        Rank rank = null;
        for (Rank cachedRank : cachedRanks) {
            if (cachedRank.getWeight() <= highestWeight)
                continue;
            rank = cachedRank;
            highestWeight = cachedRank.getWeight();
        }
        bestRank = rank;
    }

    /**
     * Get a list of our ranks' ids
     *
     * @return A {@link Collection} of {@link String}
     */
    public Collection<String> getRankIds() {
        return ranks.keySet();
    }

    /**
     * Add a permanent rank
     *
     * @param rank Target rank
     */
    public void addRank(Rank rank) {
        addRank(rank, -1L);
    }

    /**
     * Add a temporary rank
     *
     * @param rank   Target rank
     * @param expiry Target expiry in milliseconds
     */
    public void addRank(Rank rank, long expiry) {
        if (rank == null)
            return;
        cachedRanks.add(rank);
        ranks.put(rank.getId(), (double) (System.currentTimeMillis() + expiry));
        updateBestRank();
        BPerms.getInstance().getHookManager().callHook(Hooks.PLAYER_GRANTED_RANK, new PlayerProfileRankHook(rank, this, id, cachedPlayer));
    }

    /**
     * Remove a rank
     *
     * @param rank Target rank
     * @return True, if this rank was found and removed. False, otherwise.
     */
    public boolean removeRank(Rank rank) {
        if (rank == null || ranks.remove(rank.getId()) == null)
            return false;
        cachedRanks.remove(rank);
        updateBestRank();
        BPerms.getInstance().getHookManager().callHook(Hooks.PLAYER_REVOKED_RANK, new PlayerProfileRankHook(rank, this, id, cachedPlayer));
        return true;
    }

    /**
     * Get our best rank by weight
     *
     * @return A not-null {@link Rank}
     */
    @NotNull
    public Rank getBestRank() {
        if (bestRank == null)
            updateBestRank();
        return bestRank;
    }

    /**
     * Get a list of our ranks
     *
     * @return A {@link Collection} of {@link Rank}
     */
    public Collection<Rank> getRanks() {
        return cachedRanks;
    }
}
