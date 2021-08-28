package com.birthdates.bperms.command.resolver.impl;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.command.resolver.PermissibleResolver;
import com.birthdates.bperms.data.Rank;

/**
 * {@link PermissibleResolver} of {@link Rank}
 */
public class RankResolver implements PermissibleResolver<Rank> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Rank getPermissible(String arg) {
        return BPerms.getInstance().getRankManager().getRankById(arg.toLowerCase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUnknownMessage() {
        return "&cA rank with that name could not be found.";
    }
}
