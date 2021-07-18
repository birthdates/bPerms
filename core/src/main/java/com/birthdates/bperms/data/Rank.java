package com.birthdates.bperms.data;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.hook.Hooks;
import com.birthdates.bperms.hook.type.RankHook;
import com.birthdates.bperms.permission.ServerPermissible;

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

    /**
     * Call the change hook
     */
    private void callChangeHook() {
        BPerms.getInstance().getHookManager().callHook(Hooks.RANK_CHANGED, rankHook);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLoaded() {
        BPerms.getInstance().getHookManager().callHook(Hooks.RANK_LOADED, rankHook);
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
