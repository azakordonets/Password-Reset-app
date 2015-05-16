package com.azakordonets.tests;

import com.azakordonets.web.entities.TokensPool;
import com.azakordonets.web.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class TokensPoolTest {


    @Test
    public void addTokenTest() {
        final User user = new User("test.gmail.com");
        final String token = "123";
        final TokensPool tokensPool = new TokensPool();
        tokensPool.addToken(token, user);
        assertEquals(user, tokensPool.getUser(token));
    }

    @Test
    public void addTokenTwiceTest() {
        final User user = new User("test.gmail.com");
        final String token = "123";
        final TokensPool tokensPool = new TokensPool();
        tokensPool.addToken(token, user);
        tokensPool.addToken(token, user);
        assertEquals(1, tokensPool.size());
    }

    @Test
    public void remoteTokenTest() {
        final User user = new User("test.gmail.com");
        final String token = "123";
        final TokensPool tokensPool = new TokensPool();
        tokensPool.addToken(token, user);
        assertEquals(user, tokensPool.getUser(token));
        tokensPool.removeToken(token);
        assertEquals(0, tokensPool.size());
        assertNull(tokensPool.getUser(token));
    }
}
