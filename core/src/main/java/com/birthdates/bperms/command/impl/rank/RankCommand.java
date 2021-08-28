package com.birthdates.bperms.command.impl.rank;

import com.birthdates.bperms.command.PermissibleArgumentCommand;
import com.birthdates.bperms.command.impl.rank.args.RankAddArgument;
import com.birthdates.bperms.command.impl.rank.args.RankListArgument;
import com.birthdates.bperms.command.impl.rank.args.RankRemoveArgument;
import com.birthdates.bperms.command.impl.rank.args.RankWeightArgument;
import com.birthdates.bperms.command.resolver.PermissibleResolver;
import com.birthdates.bperms.command.resolver.impl.RankResolver;
import com.birthdates.bperms.data.Rank;

/**
 * Rank command (/rank)
 */
public class RankCommand extends PermissibleArgumentCommand<Rank> {

    /**
     * Our resolver
     */
    private static final RankResolver RESOLVER = new RankResolver();

    public RankCommand() {
        super("rank", "bperms.rank", 1);

        // Register our specific arguments
        registerArg(new RankAddArgument(), "<rank> - Create a rank with that name", "add", "create");
        registerArg(new RankRemoveArgument(), "<rank> - Remove a rank with that name", "remove", "delete");
        registerArg(new RankListArgument(), "- List all ranks", "list", "all");
        registerArg((RankWeightArgument<Rank>) () -> RESOLVER, "<rank> <weight> - Set the weight of this rank (decimal)", "weight");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PermissibleResolver<Rank> getResolver() {
        return RESOLVER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPermissibleName() {
        return "rank";
    }
}
