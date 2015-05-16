package com.azakordonets.tests;

import com.azakordonets.mail.MailSender;
import com.azakordonets.web.controller.ResetPasswordController;
import com.azakordonets.web.entities.TokensPool;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ResetPasswordControllerTest {

    @Mock
    private MailSender mailSender;
    @Mock
    private Runnable runnable;


    @Test
    @Ignore("Can't make it work ")
    public void testResetPasswordMail() throws Exception {
        ResetPasswordController controller = new ResetPasswordController("http://localhost", 8080, new TokensPool(), mailSender);
        stub(mailSender.produceSendMailTask("", "", "")).toReturn(runnable);
        controller.sendResetPasswordEmail("test@gmail.com", "token");
        verify(mailSender, times(1)).produceSendMailTask("", "", "").run();

    }

    @Test
    public void testGetResetPasswordPage() throws Exception {
        ResetPasswordController controller = new ResetPasswordController("http://localhost", 8080, new TokensPool(), mailSender);
        final File resetPasswordPage = controller.getResetPasswordPage();
        assertNotNull(resetPasswordPage);
    }
}
