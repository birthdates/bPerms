package com.birthdates.bperms.bukkit.manager;

import com.birthdates.bperms.bukkit.BPermsBukkit;
import com.birthdates.bperms.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
public class BukkitPlayerManager extends PlayerManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(Object player, String message) {
        BPermsBukkit.validateSender(player);
        ((CommandSender) player).sendMessage(translate(message));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(Object player, String permission) {
        BPermsBukkit.validateSender(player);
        return ((CommandSender) player).hasPermission(permission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kickPlayer(Object player, String reason) {
        BPermsBukkit.validatePlayer(player);
        ((Player) player).kickPlayer(translate(reason));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPlayer(UUID id) {
        return Bukkit.getPlayer(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getId(Object player) {
        BPermsBukkit.validatePlayer(player);
        return ((Player) player).getUniqueId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName(UUID id) {
        return Bukkit.getOfflinePlayer(id).getName();
    }

    @Override
    public String getName(Object player) {
        BPermsBukkit.validateSender(player);
        return ((CommandSender) player).getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Object> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

}
