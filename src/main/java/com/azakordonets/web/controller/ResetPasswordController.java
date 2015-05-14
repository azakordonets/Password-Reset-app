package com.azakordonets.web.controller;

import com.azakordonets.mail.MailSender;
import com.azakordonets.web.entities.TokensPool;
import com.azakordonets.web.entities.User;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Andrew Zakordonets
 * Date : 12/15/2015.
 * <p>
 */
public class ResetPasswordController {

    private static final Logger log = LogManager.getLogger(ResetPasswordController.class);
    private final MailSender mailSender;
    private final String url;
    private final int port;
    private final TokensPool tokensPool;
    private final String body;

    public ResetPasswordController(String url, int port, TokensPool tokensPool) throws IOException {
        this.mailSender = new MailSender();
        this.url = url;
        this.port = port;

        URL bodyUrl = Resources.getResource("messageBody.txt");
        this.body = Resources.toString(bodyUrl, Charsets.UTF_8);
        this.tokensPool = tokensPool;

    }

    public void sendResetPasswordEmail(String email, String token) {
        User user = new User(email);
        tokensPool.addToken(token, user);
        final String resetUrl = String.format("%s:%d/landing?token=%s&email=%s", url, port, token, email);
        final String message = String.format(body, resetUrl);
        log.info("Sending token to {} address", email);
        mailSender.produceSendMailTask(email, "Password reset request", message).run();
    }

    public void invoke(String token, String password, String email) {

    }

    public URI getResetPasswordRedirectUrl() throws URISyntaxException {
        return this.getClass().getResource("/html/enterNewPassword.html").toURI();
    }
}
