package com.birthdates.bperms.command;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.data.Rank;

/**
 * {@link PermissibleResolver} of {@link Rank}
 */
public class RankFinder implements PermissibleResolver<Rank> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Rank getPermissible(String arg) {
        return BPerms.getInstance().getRankManager().getRankById(arg.toLowerCase());
    }
}
