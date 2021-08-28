package com.birthdates.bperms.command.resolver.impl;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.command.resolver.PermissibleResolver;
import com.birthdates.bperms.data.Profile;

/**
 * {@link PermissibleResolver} of {@link Profile}
 */
public class ProfileResolver implements PermissibleResolver<Profile> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Profile getPermissible(String arg) {
        return BPerms.getInstance().getPlayerManager().getProfile(BPerms.getInstance().getPlayerManager().getId(arg));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUnknownMessage() {
        return "&cA player with that name could not be found.";
    }
}
