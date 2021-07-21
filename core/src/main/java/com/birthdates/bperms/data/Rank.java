package com.birthdates.bperms.data;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.hook.Hooks;
import com.birthdates.bperms.hook.type.RankHook;
import com.birthdates.bperms.permission.ServerPermissible;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Rank extends ServerPermissible {

    /**
     * Our unique ID
     */
    private transient final String id;
    /**
     * Our unique rank hook
     */
    private transient final RankHook rankHook = new RankHook(this);
    /**
     * Our name
     */
    private String name = "Untitled";
    /**
     * Our color
     */
    private String color = "&f";
    /**
     * Our weight
     */
    private double weight = 0.0D;

    /**
     * Our inheritance
     */
    private Set<String> inheritance = new HashSet<>();
    /**
     * Our cached inheritance
     */
    private transient Set<Rank> cachedInheritance = new HashSet<>();

    /**
     * Are we the default rank?
     */
    private boolean defaultRank;

    public Rank(String id) {
        this.id = id;
    }

    public Rank(String id, String name) {
        this(id);
        this.name = name;
    }

    public Collection<Rank> getInheritedRanks() {
        Collection<Rank> ranks = new ArrayList<>();
        getInheritedRanks(ranks);
        return ranks;
    }

    private void getInheritedRanks(Collection<Rank> ranks) {
        for (String id : inheritance) {
            Rank rank = BPerms.getInstance().getRankManager().getRankById(id);
            if (rank == null)
                continue;
            ranks.add(rank);
            rank.getInheritedRanks(ranks);
        }
    }

    /**
     * Are we inherited with a rank?
     *
     * @param rank Target rank
     * @return True, if we are. False, otherwise.
     */
    public boolean isInheritedWith(Rank rank) {
        if (inheritance.contains(rank.getId()))
            return true;

        for (Rank otherRank : cachedInheritance) {
            if (otherRank.inheritance.contains(rank.id) || otherRank.isInheritedWith(rank))
                return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fillCachedPermissions() {
        super.fillCachedPermissions();
        for (Rank rank : cachedInheritance) {
            cachedPermissions.addAll(rank.getAllPermissions());
        }
    }


    /**
     * Call the change hook with Redis call
     */
    public void callChangeHook() {
        callChangeHook(true);
    }

    /**
     * Call the change hook
     *
     * @param redis Send update through Redis?
     */
    public void callChangeHook(boolean redis) {
        callChangeHook(redis, Hooks.RANK_CHANGED);
    }

    /**
     * Call the change hook
     *
     * @param redis Send update through Redis?
     * @param hook  Type of hook
     */
    public void callChangeHook(boolean redis, Hooks hook) {
        BPerms.getInstance().getHookManager().callHook(hook, rankHook);

        if (redis && BPerms.getInstance().getRankManager() != null)
            BPerms.getInstance().getRankManager().sendUpdate(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPermission(String server, String permission, long expiry) {
        super.addPermission(server, permission, expiry);
        callChangeHook(true, Hooks.RANK_CHANGED_PERMISSIONS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removePermission(String server, String permission) {
        boolean ret = super.removePermission(server, permission);
        if (ret)
            callChangeHook(true, Hooks.RANK_CHANGED_PERMISSIONS);
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoaded() {
        BPerms.getInstance().getHookManager().callHook(Hooks.RANK_LOADED, rankHook);
        cachedInheritance = inheritance.stream()
                .map(id -> BPerms.getInstance().getRankManager().getRankById(id))
                .filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * Inherit a rank
     *
     * @param rank Target rank
     * @return True, if we've inherited this rank. False otherwise.
     */
    public boolean inherit(Rank rank) {
        if (!inheritance.add(rank.id))
            return false;
        cachedInheritance.add(rank);
        return true;
    }

    /**
     * Un-inherit a rank
     *
     * @param rank Target rank
     * @return True, if this rank was un-inherited. False, otherwise.
     */
    public boolean unInherit(Rank rank) {
        if (!inheritance.remove(rank.id))
            return false;
        cachedInheritance.remove(rank);
        return true;
    }

    /**
     * Get our name
     *
     * @return A {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Set our name
     *
     * @param name New name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set our prefix
     *
     * @param prefix New prefix
     */
    public void setPrefix(String prefix) {
        super.setPrefix(prefix);
        callChangeHook();
    }

    /**
     * Set our suffix
     *
     * @param suffix New suffix
     */
    public void setSuffix(String suffix) {
        super.setSuffix(suffix);
        callChangeHook();
    }

    /**
     * Get our formatted color
     *
     * @return A {@link String}
     */
    public String getColor() {
        return BPerms.getInstance().getPlayerManager().translate(color);
    }

    /**
     * Set our color
     *
     * @param color New color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Get our formatted colored name
     *
     * @return A {@link String}
     */
    public String getColoredName() {
        return getColor() + getName();
    }

    /**
     * Are we the default rank?
     *
     * @return True, if so. False, otherwise.
     */
    public boolean isDefaultRank() {
        return defaultRank;
    }

    /**
     * Set if we're the default rank
     *
     * @param defaultRank New value
     */
    public void setDefaultRank(boolean defaultRank) {
        this.defaultRank = defaultRank;
    }

    /**
     * Get our weight
     *
     * @return A {@link Double}
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Set our weight
     *
     * @param weight New weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespace() {
        return "ranks";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getType() {
        return Rank.class;
    }
}
