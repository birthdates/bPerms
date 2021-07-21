package com.birthdates.bperms.command;

import com.birthdates.bperms.permission.Permissible;

/**
 * Find object of type T
 *
 * @param <T> Target type that extends {@link Permissible}
 */
public interface PermissibleResolver<T extends Permissible> {

    /**
     * Get our {@link Permissible}
     *
     * @param arg Target arg
     * @return A {@link Permissible} or T
     */
    T getPermissible(String arg);

}
