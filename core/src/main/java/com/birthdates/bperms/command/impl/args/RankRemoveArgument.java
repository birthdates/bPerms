package com.birthdates.bperms.command.impl.args;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.command.ArgFunction;
import org.apache.commons.lang3.StringUtils;

/**
 * Rank remove command (/rank remove)
 */
public class RankRemoveArgument implements ArgFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public void executed(Object player, String label, String[] args) {
        if (args.length < 1) {
            BPerms.getInstance().getPlayerManager().sendMessage(player, "&cPlease specify a rank name.");
            return;
        }

        // Try to remove rank
        String name = StringUtils.join(args, " ");
        if (!BPerms.getInstance().getRankManager().removeRank(name)) {
            BPerms.getInstance().getPlayerManager().sendMessage(player, "&cNo rank with that id or name was found.");
            return;
        }

        BPerms.getInstance().getPlayerManager().sendMessage(player, "&aYou have removed that rank.");
    }
}

