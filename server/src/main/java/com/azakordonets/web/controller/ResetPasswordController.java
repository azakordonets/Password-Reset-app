package com.azakordonets.web.controller;

import com.azakordonets.notifications.mail.MailSender;
import com.azakordonets.utils.ServerProperties;
import com.azakordonets.web.entities.TokensPool;
import com.azakordonets.web.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Andrew Zakordonets
 * Date : 12/15/2015.
 * <p>
 */
public class ResetPasswordController {

    private static final Logger log = LogManager.getLogger(ResetPasswordController.class);
    private final MailSender mailSender;
    private String protocol;
    private String host;
    private int port;

    public ResetPasswordController() throws IOException {
        this.mailSender = new MailSender();
        ServerProperties props = new ServerProperties();
        protocol = props.getProperty("protocol");
        host = props.getProperty("host");
        port = props.getIntProperty("port");
    }

    public void sendResetPasswordEmail(String email, String token) {
        User user = new User(email);
        TokensPool.addToken(token, user);
        final String resetUrl = String.format("%s%s:%s/landing?token=%s&email=%s", protocol, host, port, token, email);
        final String message = String.format("You have requested password reset. Please folow this link %s", resetUrl);
        log.info("Sending token to {} address", email);
        mailSender.produceSendMailTask(email, "Password reset request", message).run();
    }

    public void invoke(String token, String password, String email) {

    }

    public URI getResetPasswordRedirectUrl() throws URISyntaxException {
        return this.getClass().getResource("/html/enterNewPassword.html").toURI();
    }
}
