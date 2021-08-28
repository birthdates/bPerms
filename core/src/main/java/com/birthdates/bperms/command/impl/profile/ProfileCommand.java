package com.birthdates.bperms.command.impl.profile;

import com.birthdates.bperms.command.PermissibleArgumentCommand;
import com.birthdates.bperms.command.resolver.PermissibleResolver;
import com.birthdates.bperms.command.resolver.impl.ProfileResolver;
import com.birthdates.bperms.data.Profile;

/**
 * Command for editing {@link Profile}
 */
public class ProfileCommand extends PermissibleArgumentCommand<Profile> {

    /**
     * Our resolver
     */
    private static final PermissibleResolver<Profile> RESOLVER = new ProfileResolver();

    public ProfileCommand() {
        super("profile", "bperms.profile", 1);

        // Register our specific arguments
        registerArg((ProfileGrantArgument) () -> RESOLVER, "<" + getPermissibleName() + "> <rank> (server) (time) - Grant a user a rank for a certain server (or all) and for a certain time (or forever)", "grant", "addrank");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PermissibleResolver<Profile> getResolver() {
        return RESOLVER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPermissibleName() {
        return "name|uuid";
    }
}
