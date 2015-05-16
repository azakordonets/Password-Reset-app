package com.azakordonets.tests;

import com.azakordonets.web.entities.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserTest {

    @Test
    public void testUserEmail() {
        final String email = "test@gmail.com";
        final User user = new User(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testUserPassword(){
        final User user = new User("test@gmail.com");
        assertEquals("", user.getNewPassword());
        final String password = "password";
        user.setNewPassword(password);
        assertEquals(password, user.getNewPassword());
    }

    @Test
    public void testUserToken() {
        final User user = new User("test@gmail.com");
        assertEquals("", user.getNewPassword());
        final long resetPasswordTokenTs = 123L;
        user.setResetPasswordTokenTs(resetPasswordTokenTs);
        assertEquals(resetPasswordTokenTs, user.getResetPasswordTokenTs());
    }
}
