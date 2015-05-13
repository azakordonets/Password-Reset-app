package com.azakordonets.web.entities;

import com.azakordonets.utils.ServerProperties;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public final class TokensPool {

    private static final Logger log = LogManager.getLogger(TokensPool.class);

    private TokensPool() {
    }

    private static class Holder {
        static final ServerProperties props = new ServerProperties();
        static final Cache<String, User> cache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .expireAfterWrite(props.getIntProperty("tokenExpiration"), TimeUnit.MINUTES)
                .build();
        static final ConcurrentMap<String, User> tokensPool = cache.asMap();
    }

    public static ConcurrentMap<String, User> getInstance() {
        return Holder.tokensPool;
    }

    public static void addToken(String token, User user) {
        log.info("Adding token for {} user to the pool", user.getEmail());
        if (TokensPool.tokenExists(token)) {
            final User oldUser = getUser(token);
            TokensPool.getInstance().replace(token, oldUser, user);
        } else {
            TokensPool.getInstance().put(token, user);
        }
    }

    public static User getUser(String token) {
        return TokensPool.getInstance().get(token);
    }

    public static void removeToken(String token) {
        TokensPool.getInstance().remove(token);
    }

    public static boolean tokenExists(String token) {
        return TokensPool.getInstance().containsKey(token);
    }

}
