package com.birthdates.bperms.manager;

import com.birthdates.bperms.data.PermissionGroup;
import com.birthdates.redisdata.data.RedisDataManager;
import org.jetbrains.annotations.Nullable;

/**
 * Permission group manager (data manager)
 */
public class PermissionGroupManager extends RedisDataManager<PermissionGroup> {

    public PermissionGroupManager() {
        try {
            loadAll("permission_groups", PermissionGroup.class.getConstructor(String.class));
        } catch (NoSuchMethodException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Get a {@link PermissionGroup} by it's id
     *
     * @param id Target id
     * @return A nullable {@link PermissionGroup}
     */
    @Nullable
    public PermissionGroup getPermissionGroupById(String id) {
        return getData().getOrDefault(id, null);
    }
}
