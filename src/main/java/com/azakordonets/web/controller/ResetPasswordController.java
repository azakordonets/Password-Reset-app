package com.azakordonets.web.controller;

import com.azakordonets.mail.MailSender;
import com.azakordonets.web.entities.TokensPool;
import com.azakordonets.web.entities.User;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;

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
    private final String pageContent;

    public ResetPasswordController(String url, int port, TokensPool tokensPool) throws Exception {
        this(url, port, tokensPool, new MailSender());
    }

    public ResetPasswordController(String url, int port, TokensPool tokensPool, MailSender mailSender) throws Exception {
        this.mailSender = mailSender;
        this.url = url;
        this.port = port;

        URL bodyUrl = Resources.getResource("messageBody.txt");
        this.body = Resources.toString(bodyUrl, Charsets.UTF_8);
        this.tokensPool = tokensPool;
        File page = new File(this.getClass().getResource("/html/enterNewPassword.html").toURI());
        this.pageContent = readFile(page, Charset.forName("UTF-8"));
    }

    private static String readFile(File file, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(file.toPath());
        return new String(encoded, encoding);
    }

    public void sendResetPasswordEmail(String email, String token) {
        User user = new User(email);
        tokensPool.addToken(token, user);
        //todo for port 80 no need in port.
        final String resetUrl = String.format("%s:%d/landing?token=%s", url, port, token);
        final String message = String.format(body, resetUrl);
        log.info("Sending token to {} address", email);
        mailSender.produceSendMailTask(email, "Password reset request", message).run();
    }

    public void invoke(String token, String password, String email) {

    }

    public String getResetPasswordPage(String email, String token) {
        return pageContent.replace("{EMAIL}", email).replace("{TOKEN}", token);
    }
}
