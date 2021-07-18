package com.birthdates.bperms.bungee.manager;

import com.birthdates.bperms.bungee.BPermsBungee;
import com.birthdates.bperms.manager.PlayerManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class BungeePlayerManager extends PlayerManager {

    /**
     * Create a {@link BaseComponent} from text
     *
     * @param text Target text
     * @return A {@link BaseComponent}
     */
    private BaseComponent createComponent(String text) {
        return new TextComponent(translate(text));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(Object player, String message) {
        BPermsBungee.validateSender(player);
        ((CommandSender) player).sendMessage(createComponent(message));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(Object player, String permission) {
        BPermsBungee.validateSender(player);
        return ((CommandSender) player).hasPermission(permission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kickPlayer(Object player, String reason) {
        BPermsBungee.validatePlayer(player);
        ((ProxiedPlayer) player).disconnect(createComponent(reason));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPlayer(UUID id) {
        return ProxyServer.getInstance().getPlayer(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getId(Object player) {
        BPermsBungee.validatePlayer(player);
        return ((ProxiedPlayer) player).getUniqueId();
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
    public Collection<Object> getOnlinePlayers() {
        return new ArrayList<>(ProxyServer.getInstance().getPlayers());
    }

}
