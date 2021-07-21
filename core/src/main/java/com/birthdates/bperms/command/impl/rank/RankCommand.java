package com.birthdates.bperms.command.impl.rank;

import com.birthdates.bperms.command.ArgumentCommand;
import com.birthdates.bperms.command.RankFinder;
import com.birthdates.bperms.command.impl.args.PrefixArgument;
import com.birthdates.bperms.command.impl.rank.args.RankAddArgument;
import com.birthdates.bperms.command.impl.rank.args.RankListArgument;
import com.birthdates.bperms.command.impl.rank.args.RankRemoveArgument;
import com.birthdates.bperms.data.Rank;

/**
 * Rank command (/rank)
 */
public class RankCommand extends ArgumentCommand {

    private static final RankFinder rankFinder = new RankFinder();

    public RankCommand() {
        super("rank", "bperms.rank", 1);

        registerArg(new RankAddArgument(), "<rank> - Create a rank with that name", "add", "create");
        registerArg(new RankRemoveArgument(), "<rank> - Remove a rank with that name", "remove", "delete");
        registerArg(new RankListArgument(), "- List all ranks", "list", "all");
        registerArg((PrefixArgument<Rank>) () -> rankFinder, "<rank> <prefix> - Set a prefix of a rank", "prefix");
    }
}
