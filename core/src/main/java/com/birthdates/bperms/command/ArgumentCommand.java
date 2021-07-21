package com.birthdates.bperms.command;

import com.birthdates.bperms.BPerms;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Command with multiple arguments
 */
public abstract class ArgumentCommand extends Command {

    /**
     * Set of {@link Argument}
     */
    private final Set<Argument> args = new LinkedHashSet<>();

    public ArgumentCommand(String name, String permission, int minArgs) {
        super(name, permission, "", minArgs);

        registerArg((player, label, args) -> sendUsageMessage(player, label), "- Send this help message", "help");
    }

    /**
     * Register an argument
     *
     * @param command         Callback function
     * @param argsDescription Description of argument
     * @param aliases         Argument's aliases
     */
    protected void registerArg(ArgFunction command, String argsDescription, String... aliases) {
        if (aliases.length == 0)
            throw new IllegalStateException("No arguments?");
        this.args.add(new Argument(command, argsDescription, aliases));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onExecuted(Object player, String label, String[] args) {
        // Try find arg
        String firstArg = args[0].toLowerCase();
        ArgFunction foundArg = null;
        for (Argument argument : this.args) {
            for (String arg : argument.aliases) {
                if (!arg.equalsIgnoreCase(firstArg))
                    continue;
                foundArg = argument.argFunction;
            }
        }
        if (foundArg == null) {
            sendUsageMessage(player, label);
            return;
        }

        // Execute arg
        foundArg.executed(player, label, ArrayUtils.remove(args, 0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendUsageMessage(Object player, String label) {
        String line = "&7&m" + StringUtils.repeat("-", 32);
        BPerms.getInstance().getPlayerManager().sendMessage(player, line);
        BPerms.getInstance().getPlayerManager().sendMessage(player, BPerms.getInstance().getColor() + "&lRank Help");
        BPerms.getInstance().getPlayerManager().sendMessage(player, "");
        for (Argument entry : args) {
            String name = String.join("|", entry.aliases);
            BPerms.getInstance().getPlayerManager().sendMessage(player, "&7/" + label + " &f" + name + "&f " + entry.argsDescription);
        }
        BPerms.getInstance().getPlayerManager().sendMessage(player, "");
        BPerms.getInstance().getPlayerManager().sendMessage(player, line);
    }

    /**
     * Class for arguments
     */
    private static class Argument {
        /**
         * Our callback function
         */
        private final ArgFunction argFunction;
        /**
         * Our description
         */
        private final String argsDescription;
        /**
         * Our aliases
         */
        private final String[] aliases;

        public Argument(ArgFunction argFunction, String argsDescription, String[] aliases) {
            this.argFunction = argFunction;
            this.argsDescription = argsDescription;
            this.aliases = aliases;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() != Argument.class)
                return false;

            // Try to find conflicting alias
            Argument argument = (Argument) obj;
            for (String arg : aliases) {
                for (String arg2 : argument.aliases) {
                    if (arg.equalsIgnoreCase(arg2))
                        return true;
                }
            }

            return super.equals(obj);
        }
    }
}
