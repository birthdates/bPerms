package com.birthdates.bperms.command.impl;

import com.birthdates.bperms.command.ArgumentCommand;
import com.birthdates.bperms.command.impl.args.RankAddArgument;
import com.birthdates.bperms.command.impl.args.RankListArgument;
import com.birthdates.bperms.command.impl.args.RankRemoveArgument;

/**
 * Rank command (/rank)
 */
public class RankCommand extends ArgumentCommand {

    public RankCommand() {
        super("rank", "bperms.rank", 1);

        registerArg(new RankAddArgument(), "<rank> - Create a rank with that name", "add", "create");
        registerArg(new RankRemoveArgument(), "<rank> - Remove a rank with that name", "remove", "delete");
        registerArg(new RankListArgument(), "- List all ranks", "list", "all");
    }
}
