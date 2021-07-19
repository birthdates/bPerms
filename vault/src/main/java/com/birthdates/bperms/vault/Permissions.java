package com.birthdates.bperms.vault;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.data.Profile;
import com.birthdates.bperms.data.Rank;
import net.milkbowl.vault.permission.Permission;

import static com.birthdates.bperms.vault.VaultHook.getProfile;

/**
 * Vault permissions hook
 */
public class Permissions extends Permission {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "bPerms-Vault-Permissions";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasSuperPermsCompat() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean playerHas(String world, String player, String permission) {
        return getProfile(player).getAllPermissions().contains(permission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean playerAdd(String world, String player, String permission) {
        Profile profile = getProfile(player);
        profile.addPermission(permission);
        profile.saveAsync();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean playerRemove(String world, String player, String permission) {
        Profile profile = getProfile(player);
        boolean ret = profile.removePermission(permission);
        if (ret)
            profile.saveAsync();
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean groupHas(String world, String group, String permission) {
        Rank rank = BPerms.getInstance().getRankManager().getRankById(group);
        if (rank == null)
            return false;
        return rank.getAllPermissions().contains(permission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean groupAdd(String world, String group, String permission) {
        Rank rank = BPerms.getInstance().getRankManager().getRankById(group);
        if (rank == null)
            return false;
        rank.addPermission(permission);
        rank.saveAsync();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean groupRemove(String world, String group, String permission) {
        Rank rank = BPerms.getInstance().getRankManager().getRankById(group);
        boolean ret = rank != null && rank.removePermission(permission);
        if (ret)
            rank.saveAsync();
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean playerInGroup(String world, String player, String group) {
        return getProfile(player).getRankIds().contains(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean playerAddGroup(String world, String player, String group) {
        Profile profile = getProfile(player);
        profile.addRank(BPerms.getInstance().getRankManager().getRankById(group));
        profile.saveAsync();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean playerRemoveGroup(String world, String player, String group) {
        Profile profile = getProfile(player);
        boolean ret = getProfile(player).removeRank(BPerms.getInstance().getRankManager().getRankById(group));
        if (ret)
            profile.saveAsync();
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getPlayerGroups(String world, String player) {
        return getProfile(player).getRankIds().toArray(new String[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrimaryGroup(String world, String player) {
        Profile profile = getProfile(player);
        return profile == null ? "default" : profile.getBestRank().getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getGroups() {
        return BPerms.getInstance().getRankManager().getSortedRanks().stream().map(Rank::getId).toArray(String[]::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasGroupSupport() {
        return true;
    }
}
