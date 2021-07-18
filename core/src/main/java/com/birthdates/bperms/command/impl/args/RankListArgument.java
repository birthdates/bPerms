package com.birthdates.bperms.command.impl.args;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.command.ArgFunction;
import com.birthdates.bperms.data.Rank;

/**
 * Rank list command (/rank list)
 */
public class RankListArgument implements ArgFunction {

    /**
     * {@inheritDoc}
     */
    @Override
    public void executed(Object player, String label, String[] args) {
        BPerms.getInstance().getPlayerManager().sendMessage(player, BPerms.getInstance().getColor() + "Ranks:");

        // Send seperate messages for each rank
        for (Rank rank : BPerms.getInstance().getRankManager().getSortedRanks()) {
            BPerms.getInstance().getPlayerManager().sendMessage(player, " &f- " + rank.getColoredName() + " &6✰");
        }
    }
}
