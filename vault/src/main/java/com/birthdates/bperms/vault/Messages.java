package com.birthdates.bperms.vault;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.data.Profile;
import com.birthdates.bperms.data.Rank;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import static com.birthdates.bperms.vault.VaultHook.getProfile;

/**
 * Vault chat hook
 */
public class Messages extends Chat {

    /**
     * {@inheritDoc}
     */
    public Messages(Permission perms) {
        super(perms);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "bPerms-Vault-Chat";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlayerPrefix(String world, String player) {
        return getProfile(player).getPrefix();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerPrefix(String world, String player, String prefix) {
        Profile profile = getProfile(player);
        profile.setPrefix(prefix);
        profile.saveAsync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlayerSuffix(String world, String player) {
        return getProfile(player).getSuffix();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerSuffix(String world, String player, String suffix) {
        Profile profile = getProfile(player);
        profile.setSuffix(suffix);
        profile.saveAsync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGroupPrefix(String world, String group) {
        Rank rank = BPerms.getInstance().getRankManager().getRankById(group);
        if (rank == null)
            return "";
        return rank.getPrefix();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGroupPrefix(String world, String group, String prefix) {
        Rank rank = BPerms.getInstance().getRankManager().getRankById(group);
        if (rank == null)
            return;
        rank.setPrefix(prefix);
        rank.saveAsync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGroupSuffix(String world, String group) {
        Rank rank = BPerms.getInstance().getRankManager().getRankById(group);
        if (rank == null)
            return "";
        return rank.getSuffix();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGroupSuffix(String world, String group, String suffix) {
        Rank rank = BPerms.getInstance().getRankManager().getRankById(group);
        if (rank == null)
            return;
        rank.setSuffix(suffix);
        rank.saveAsync();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayerInfoInteger(String s, String s1, String s2, int i) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerInfoInteger(String s, String s1, String s2, int i) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getGroupInfoInteger(String s, String s1, String s2, int i) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGroupInfoInteger(String s, String s1, String s2, int i) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getPlayerInfoDouble(String s, String s1, String s2, double v) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerInfoDouble(String s, String s1, String s2, double v) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getGroupInfoDouble(String s, String s1, String s2, double v) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGroupInfoDouble(String s, String s1, String s2, double v) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getPlayerInfoBoolean(String s, String s1, String s2, boolean b) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerInfoBoolean(String s, String s1, String s2, boolean b) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getGroupInfoBoolean(String s, String s1, String s2, boolean b) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGroupInfoBoolean(String s, String s1, String s2, boolean b) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlayerInfoString(String s, String s1, String s2, String s3) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPlayerInfoString(String s, String s1, String s2, String s3) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGroupInfoString(String s, String s1, String s2, String s3) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGroupInfoString(String s, String s1, String s2, String s3) {

    }
}
