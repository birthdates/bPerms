package com.birthdates.bperms.vault;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.data.Profile;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

import java.util.UUID;

/**
 * Our vault hook utility
 */
public class VaultHook {

    /**
     * Initialize our vault hook
     *
     * @param plugin Owning plugin
     */
    public static void init(Plugin plugin) {
        Permission permission = new Permissions();
        Bukkit.getServer().getServicesManager().register(Permission.class, permission, plugin, ServicePriority.Highest);
        Bukkit.getServer().getServicesManager().register(Chat.class, new Messages(permission), plugin, ServicePriority.Highest);
        BPerms.log("Vault hook initiated.");
    }

    /**
     * Utility to get a user's profile from their username
     *
     * @param player Target player
     * @return A {@link Profile} if found
     */
    public static Profile getProfile(String player) {
        UUID id = Bukkit.getOfflinePlayer(player).getUniqueId();
        return BPerms.getInstance().getPlayerManager().getProfile(id);
    }
}
