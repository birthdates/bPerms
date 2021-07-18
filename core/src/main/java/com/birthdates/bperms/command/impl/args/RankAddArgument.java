package com.birthdates.bperms.command.impl.args;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.command.ArgFunction;
import com.birthdates.bperms.data.Rank;
import org.apache.commons.lang3.StringUtils;

/**
 * Rank add command (/rank add)
 */
public class RankAddArgument implements ArgFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public void executed(Object player, String label, String[] args) {
        if (args.length < 1) {
            BPerms.getInstance().getPlayerManager().sendMessage(player, "&cPlease specify a rank name.");
            return;
        }

        // Try to find conflicting rank
        String name = StringUtils.join(args, " ");
        String id = name.toLowerCase().replaceAll(" ", "_");
        if (BPerms.getInstance().getRankManager().getRankById(id) != null) {
            BPerms.getInstance().getPlayerManager().sendMessage(player, "&cA rank with that name already exists.");
            return;
        }

        // Create rank
        Rank rank = new Rank(id, name);
        BPerms.getInstance().getRankManager().addRank(rank);
        BPerms.async(rank::save);
        BPerms.getInstance().getPlayerManager().sendMessage(player, "&aYou have created the " + name + " rank!");
    }
}
