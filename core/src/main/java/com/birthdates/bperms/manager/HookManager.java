package com.birthdates.bperms.manager;

import com.birthdates.bperms.BPerms;
import com.birthdates.bperms.hook.Hook;
import com.birthdates.bperms.hook.HookEvent;
import com.birthdates.bperms.hook.Hooks;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * A hook manager
 */
public class HookManager {

    /**
     * Our hook id -> {@link List} of {@link MethodObject} map
     */
    private final Map<String, List<MethodObject>> hookMethods = new HashMap<>();

    /**
     * Register multiple hook listeners
     *
     * @param objects Array of listeners
     */
    public final void registerHookListeners(Object... objects) {
        for (Object object : objects) {
            registerHookListener(object);
        }
    }

    /**
     * Unregister multiple hook listeners
     *
     * @param objects Array of listeners
     */
    public final void unregisterHookListeners(Object... objects) {
        for (Object object : objects) {
            unregisterHookListener(object);
        }
    }

    /**
     * Register a hook listener
     *
     * @param object Target listener
     */
    public final void registerHookListener(Object object) {
        getAllValidMethods(object, (method, hookType) -> {
            List<MethodObject> hooks = hookMethods.getOrDefault(hookType.getId(), null);
            if (hooks == null)
                hookMethods.put(hookType.getId(), (hooks = new ArrayList<>()));
            hooks.add(new MethodObject(method, object));
        });
    }

    /**
     * Unregister a hook listener
     *
     * @param object Target listener
     */
    public final void unregisterHookListener(Object object) {
        getAllValidMethods(object, ((method, hookType) -> {
            List<MethodObject> hooks = hookMethods.getOrDefault(hookType.getId(), null);
            if (hooks == null)
                return;
            hooks.removeIf(obj -> obj.method.equals(method));
            if (hooks.isEmpty())
                hookMethods.remove(hookType.getId());
        }));
    }

    /**
     * Call a hook
     *
     * @param hookType  Hook type
     * @param hookEvent Hook event
     */
    public void callHook(@NotNull Hooks hookType, @NotNull HookEvent hookEvent) {
        if (!hookType.getType().isInstance(hookEvent)) {
            throw new IllegalStateException("Hook event is not the right type! Expected " + hookType.getType().getName() + ". Got: " + hookEvent.getClass().getName());
        }
        List<MethodObject> methods = hookMethods.getOrDefault(hookType.getId(), null);
        if (methods == null)
            return;
        for (MethodObject object : methods) {
            try {
                object.method.invoke(object.owner, hookEvent);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                BPerms.log("Failed to execute hook: " + hookType.getId() + ", " + hookEvent);
                exception.printStackTrace();
            }
        }
    }

    /**
     * Get all valid hook methods that match these requirements
     *
     * @param object   Target listener
     * @param callback Callback with a valid method
     */
    private void getAllValidMethods(Object object, BiConsumer<Method, Hooks> callback) {
        Class<?> type = object.getClass();
        Method[] methods = ArrayUtils.addAll(type.getDeclaredMethods(), type.getMethods());

        for (Method method : methods) {
            Hook hook = method.getAnnotation(Hook.class);
            if (hook == null)
                continue;

            Hooks hookType = hook.hook();
            if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != hookType.getType())
                continue;

            method.setAccessible(true);
            callback.accept(method, hookType);
        }
    }

    /**
     * Method object that holds instance
     */
    private static class MethodObject {
        private final Method method;
        private final Object owner;

        public MethodObject(Method method, Object owner) {
            this.method = method;
            this.owner = owner;
        }
    }
}
