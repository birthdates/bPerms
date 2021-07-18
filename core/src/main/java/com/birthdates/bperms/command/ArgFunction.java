package com.birthdates.bperms.command;

/**
 * Callback for argument command
 */
@FunctionalInterface
public interface ArgFunction {
    void executed(Object player, String label, String[] args);
}