package com.birthdates.bperms.command.impl.profile;

import com.birthdates.bperms.command.ArgFunction;
import com.birthdates.bperms.command.PermissibleArgument;
import com.birthdates.bperms.data.Profile;

public interface ProfileGrantArgument extends PermissibleArgument<Profile> {

    /**
     * {@inheritDoc}
     */
    @Override
    default ArgFunction getFunction(Profile permissible) {
        return (player, label, args) -> {
            // TODO
        };
    }


}
