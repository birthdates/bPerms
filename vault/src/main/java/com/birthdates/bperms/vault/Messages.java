package com.birthdates.bperms.vault;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.data.Profile;
import com.birthdates.bperms.data.Rank;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import static com.birthdates.bperms.vault.VaultHook.getProfile;

public class Messages extends Chat {
    public Messages(Permission perms) {
        super(perms);
    }

    @Override
    public String getName() {
        return "bPerms-Vault-Chat";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPlayerPrefix(String world, String player) {
        return getProfile(player).getPrefix();
    }

    @Override
    public void setPlayerPrefix(String world, String player, String prefix) {
        Profile profile = getProfile(player);
        profile.setPrefix(prefix);
        profile.saveAsync();
    }

    @Override
    public String getPlayerSuffix(String world, String player) {
        return getProfile(player).getSuffix();
    }

    @Override
    public void setPlayerSuffix(String world, String player, String suffix) {
        Profile profile = getProfile(player);
        profile.setSuffix(suffix);
        profile.saveAsync();
    }

    @Override
    public String getGroupPrefix(String world, String group) {
        Rank rank = BPerms.getInstance().getRankManager().getRankById(group);
        if (rank == null)
            return "";
        return rank.getPrefix();
    }

    @Override
    public void setGroupPrefix(String world, String group, String prefix) {
        Rank rank = BPerms.getInstance().getRankManager().getRankById(group);
        if (rank == null)
            return;
        rank.setPrefix(prefix);
        rank.saveAsync();
    }

    @Override
    public String getGroupSuffix(String world, String group) {
        Rank rank = BPerms.getInstance().getRankManager().getRankById(group);
        if (rank == null)
            return "";
        return rank.getSuffix();
    }

    @Override
    public void setGroupSuffix(String world, String group, String suffix) {
        Rank rank = BPerms.getInstance().getRankManager().getRankById(group);
        if (rank == null)
            return;
        rank.setSuffix(suffix);
        rank.saveAsync();
    }

    @Override
    public int getPlayerInfoInteger(String s, String s1, String s2, int i) {
        return 0;
    }

    @Override
    public void setPlayerInfoInteger(String s, String s1, String s2, int i) {

    }

    @Override
    public int getGroupInfoInteger(String s, String s1, String s2, int i) {
        return 0;
    }

    @Override
    public void setGroupInfoInteger(String s, String s1, String s2, int i) {

    }

    @Override
    public double getPlayerInfoDouble(String s, String s1, String s2, double v) {
        return 0;
    }

    @Override
    public void setPlayerInfoDouble(String s, String s1, String s2, double v) {

    }

    @Override
    public double getGroupInfoDouble(String s, String s1, String s2, double v) {
        return 0;
    }

    @Override
    public void setGroupInfoDouble(String s, String s1, String s2, double v) {

    }

    @Override
    public boolean getPlayerInfoBoolean(String s, String s1, String s2, boolean b) {
        return false;
    }

    @Override
    public void setPlayerInfoBoolean(String s, String s1, String s2, boolean b) {

    }

    @Override
    public boolean getGroupInfoBoolean(String s, String s1, String s2, boolean b) {
        return false;
    }

    @Override
    public void setGroupInfoBoolean(String s, String s1, String s2, boolean b) {

    }

    @Override
    public String getPlayerInfoString(String s, String s1, String s2, String s3) {
        return null;
    }

    @Override
    public void setPlayerInfoString(String s, String s1, String s2, String s3) {

    }

    @Override
    public String getGroupInfoString(String s, String s1, String s2, String s3) {
        return null;
    }

    @Override
    public void setGroupInfoString(String s, String s1, String s2, String s3) {

    }
}
