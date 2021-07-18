package com.birthdates.bperms.command;


import com.birthdates.bperms.BPerms;
import org.apache.commons.lang3.StringUtils;

/**
 * Abstract command
 */
public abstract class Command {

    /**
     * Our name, permission & usage
     */
    private final String name, permission, usageArgs;
    /**
     * Our minimum args
     */
    private final int minArgs;
    /**
     * Our aliases
     */
    private final String[] aliases;

    public Command(String name, String permission, String usageArgs, int minArgs, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.usageArgs = usageArgs;
        this.minArgs = minArgs;
        this.aliases = aliases;
    }

    /**
     * Get our name
     *
     * @return A {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Get our permission
     *
     * @return A {@link String}
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Get our usage arguments
     *
     * @return A {@link String}
     */
    public String getUsageArgs() {
        return usageArgs;
    }

    /**
     * Get our minimum arguments
     *
     * @return An {@link Integer}
     */
    public int getMinArgs() {
        return minArgs;
    }

    /**
     * Get our aliases
     *
     * @return An array of {@link String}
     */
    public String[] getAliases() {
        return aliases;
    }

    /**
     * Called on execution
     *
     * @param player Player who executed the command
     * @param label  Executed command's label (useful for aliases)
     * @param args   Given arguments
     */
    public final void executed(Object player, String label, String[] args) {
        if (permission != null && !StringUtils.isEmpty(permission) && !BPerms.getInstance().getPlayerManager().hasPermission(player, permission)) {
            BPerms.getInstance().getPlayerManager().sendMessage(player, "&cInsufficient permissions.");
            return;
        }

        if (args.length < minArgs) {
            sendUsageMessage(player, label);
            return;
        }

        onExecuted(player, label, args);
    }

    /**
     * Send the usage message to a player
     *
     * @param player Target player
     * @param label  Command's label
     */
    public void sendUsageMessage(Object player, String label) {
        BPerms.getInstance().getPlayerManager().sendMessage(player, "&cUsage: /" + label + " " + usageArgs);
    }

    /**
     * Called on execution
     *
     * @param player Player who executed the command
     * @param label  Executed command's label (useful for aliases)
     * @param args   Given arguments
     */
    public abstract void onExecuted(Object player, String label, String[] args);
}
