package com.birthdates.bperms.command.impl.rank.args;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.command.ArgFunction;
import com.birthdates.bperms.command.PermissibleArgument;
import com.birthdates.bperms.data.Rank;


/**
 * {@link PermissibleArgument} for {@link Rank#setWeight(double)}
 *
 * @param <T> Target permissible type
 */
public interface RankWeightArgument<T extends Rank> extends PermissibleArgument<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    default ArgFunction getFunction(T permissible) {
        return ((player, label, args) -> {
            double weight;
            try {
                weight = Double.parseDouble(args[0]);
            } catch (NumberFormatException exception) {
                BPerms.getInstance().getPlayerManager().sendMessage(player, "&cInvalid number.");
                return;
            }

            permissible.setWeight(weight);
            permissible.saveAsync();
            BPerms.getInstance().getPlayerManager().sendMessage(player, "&aYou have set the weight of " + permissible.getName() + " to "
                    + weight);
        });
    }

}
