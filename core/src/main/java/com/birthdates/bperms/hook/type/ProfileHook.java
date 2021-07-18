package com.birthdates.bperms.hook.type;

import com.birthdates.bperms.data.Profile;
import com.birthdates.bperms.hook.HookEvent;

/**
 * Profile hook
 */
public class ProfileHook extends HookEvent {

    /**
     * Our profile
     */
    private final Profile profile;

    public ProfileHook(Profile profile) {
        this.profile = profile;
    }

    /**
     * Get the profile
     *
     * @return A {@link Profile}
     */
    public Profile getProfile() {
        return profile;
    }
}
