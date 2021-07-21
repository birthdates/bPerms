package com.birthdates.bperms.sponge.manager;

import com.birthdates.bperms.manager.PlayerManager;
import com.birthdates.bperms.sponge.BPermsPlugin;
import com.birthdates.bperms.sponge.BPermsSponge;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * {@inheritDoc}
 */
public class SpongePlayerManager extends PlayerManager {

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(Object obj, String message) {
        BPermsSponge.validateSender(obj);
        ((CommandSource) obj).sendMessage(Text.of(translate(message)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPermission(Object obj, String permission) {
        BPermsSponge.validateSender(obj);
        return ((CommandSource) obj).hasPermission(permission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kickPlayer(Object obj, String reason) {
        BPermsSponge.validatePlayer(obj);
        ((Player) obj).kick(Text.of(translate(reason)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPlayer(UUID id) {
        return BPermsPlugin.getInstance().getGame().getServer().getPlayer(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID getId(Object player) {
        BPermsSponge.validatePlayer(player);
        return ((Player) player).getUniqueId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String translate(String message) {
        return TextSerializers.FORMATTING_CODE.serialize(Text.of(message));
    }

    @Override
    public String getName(UUID id) {
        User user = getUser(id).orElse(null);
        if (user == null)
            return "Unknown";
        return user.getName();
    }

    /**
     * {@inheritDoc}
     */
    public Optional<User> getUser(UUID uuid) {
        Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
        return userStorage.flatMap(storage -> storage.get(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName(Object player) {
        BPermsSponge.validateSender(player);
        return ((CommandSource) player).getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Object> getOnlinePlayers() {
        return new ArrayList<>(BPermsPlugin.getInstance().getGame().getServer().getOnlinePlayers());
    }
}
