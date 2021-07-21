package com.birthdates.bperms.redis;

import com.birthdates.bperms.BPerms;
import com.birthdates.redisdata.RedisManager;
import com.google.gson.JsonParseException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.io.Closeable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Redis pub sub listener/messenger
 */
public class RedisSub implements Closeable {

    /**
     * Our ID
     */
    private final UUID id = UUID.randomUUID();
    /**
     * Our {@link Jedis} instance
     */
    private final Jedis jedis;
    /**
     * Our {@link JedisPubSub} instance
     */
    private final JedisPubSub jedisPubSub;
    /**
     * Thread for listener
     */
    private final Thread subThread;
    /**
     * Our channel
     */
    private final String channel;
    /**
     * Current listeners/callbacks
     */
    private final Map<String, Set<Consumer<Message>>> callbacks = new HashMap<>();

    public RedisSub() {
        channel = "bPerms-" + BPerms.getInstance().getConfiguration().getServerId();
        jedis = RedisManager.getInstance().getJedis().getJedis();
        jedis.clientSetname(channel);
        jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                onData(RedisManager.getInstance().getGson().fromJson(message, Message.class));
            }
        };
        subThread = new Thread(() -> jedis.subscribe(jedisPubSub, channel));
        subThread.start();
    }

    /**
     * Register a redis listener
     *
     * @param key      Key to listen for
     * @param callback Callback with message
     */
    public final void registerListener(String key, Consumer<Message> callback) {
        Set<Consumer<Message>> callbacks = this.callbacks.getOrDefault(key, null);
        if (callbacks == null)
            this.callbacks.put(key, (callbacks = new LinkedHashSet<>()));
        callbacks.add(callback);
    }

    /**
     * Function called with a Redis message
     *
     * @param message Target message
     */
    private void onData(Message message) {
        if (message.id.equals(id.toString()))
            return;
        Set<Consumer<Message>> callbacks = this.callbacks.getOrDefault(message.type, Collections.emptySet());
        if (callbacks.isEmpty()) {
            BPerms.log("Unhandled Redis message: " + message);
            return;
        }
        for (Consumer<Message> callback : callbacks) {
            callback.accept(message);
        }
    }

    /**
     * Send a message over Redis
     *
     * @param type   Type/key
     * @param object Target object/message
     */
    public final void sendMessage(String type, Object object) {
        BPerms.async(() -> {
            try (Jedis jedis = RedisManager.getInstance().getJedis().getJedis()) {
                String data = RedisManager.getInstance().getGson().toJson(object);
                jedis.publish(channel, RedisManager.getInstance().getGson().toJson(new Message(type, data)));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        jedisPubSub.unsubscribe();
        jedis.disconnect();
        jedis.close();
        try {
            subThread.join();
        } catch (InterruptedException ignored) {

        }
    }

    /**
     * Redis message
     */
    public class Message {
        /**
         * Type/key
         */
        private final String type;
        /**
         * Object/message
         */
        private final String object;
        /**
         * It's ID
         */
        private final String id;

        public Message(String type, String object) {
            this.type = type;
            this.id = RedisSub.this.id.toString();
            this.object = object;
        }

        /**
         * Get the message/data from this class
         *
         * @param type Target data type
         * @param <T>  Data type
         * @return A {@link Object} of type T
         */
        public <T> T get(Class<T> type) {
            try {
                return RedisManager.getInstance().getGson().fromJson(object, type);
            } catch (JsonParseException ignored) {
                return null;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Message{" +
                    "type='" + type + '\'' +
                    ", object='" + object + '\'' +
                    '}';
        }
    }
}
