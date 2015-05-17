package com.azakordonets.web.entities;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public final class TokensPool {

    private static final Logger log = LogManager.getLogger(TokensPool.class);
    private static final int TOKEN_EXPIRATION_DEFAULT_PERIOD_IN_MIN = 60;
    private final ConcurrentMap<String, User> holder;

    public TokensPool() {
        Cache<String, User> cache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .expireAfterWrite(TOKEN_EXPIRATION_DEFAULT_PERIOD_IN_MIN, TimeUnit.MINUTES)
                .build();

        this.holder = cache.asMap();
    }


    public void addToken(String token, User user) {
        log.info("Adding token for {} user to the pool", user.getEmail());
        holder.put(token, user);
    }

    public User getUser(String token) {
        return holder.get(token);
    }

    public void removeToken(String token) {
        holder.remove(token);
    }

    public int size() {
        return holder.size();
    }

}
