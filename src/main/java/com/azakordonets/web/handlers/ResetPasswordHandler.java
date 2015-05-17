package com.azakordonets.web.handlers;

import com.azakordonets.utils.SHA256Util;
import com.azakordonets.web.controller.ResetPasswordController;
import com.azakordonets.web.entities.TokensPool;
import com.azakordonets.web.entities.User;
import fabricator.Fabricator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.URISyntaxException;

@Path("/")
public class ResetPasswordHandler {

    private static final Logger log = LogManager.getLogger(ResetPasswordHandler.class);

    private final ResetPasswordController resetPasswordController;
    private final TokensPool tokensPool;

    public ResetPasswordHandler(String url, int port, TokensPool tokensPool) throws Exception {
        this.resetPasswordController = new ResetPasswordController(url, port, tokensPool);
        this.tokensPool = tokensPool;
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_FORM_URLENCODED)
    @Path("resetPassword")
    public Response sendResetPasswordEmail(@FormParam("email") String email) {
        if (email == null || email.isEmpty()) {
            return badRequestResponse("Email field is empty. Please input your email.");
        } else if (!isValid(email)) {
            return badRequestResponse(String.format("%s email has not valid format.", email));
        } else {
            String token = Fabricator.alphaNumeric().hash(60);
            log.info("{} trying to reset pass.", email);
            try {
                resetPasswordController.sendResetPasswordEmail(email, token);
            } catch (Exception e) {
                log.info("Error sending mail for {}", email);
                return badRequestResponse("Error sending reset email.");
            }
            log.info("{} mail sent.", email);
            return Response.ok().entity("Email was sent").build();
        }
    }

    private boolean isValid(String email) {
        boolean isValid = false;
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            isValid = true;
        } catch (AddressException e) {
            log.error("{} is invalid e-mail address" + email);
        }
        return isValid;
    }


    private Response badRequestResponse(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(message)
                .build();
    }

    @GET
    @Produces(value = MediaType.TEXT_HTML)
    @Path("landing")
    public Response generateResetPage(@QueryParam("token") String token) throws URISyntaxException {
        User user = tokensPool.getUser(token);
        if (user != null) {
            log.info("{} landed.", user.getEmail());
            String page = resetPasswordController.getResetPasswordPage(user.getEmail(), token);
            return Response.ok(page).build();

        } else {
            return notFoundResponse(String.format("%s token was not found or it is outdated", token));
        }
    }

    @GET
    @Path("static/{filename}")
    public Response getStatic(@PathParam("filename") String filename) throws URISyntaxException {
        File file = new File(this.getClass().getResource("/html/" + filename).toURI());
        return Response.ok(file).build();
    }


    @POST
    @Consumes(value = MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(value = MediaType.APPLICATION_JSON)
    @Path("updatePassword")
    public Response updatePassword(@FormParam("password") String password,
                                   @FormParam("token") String token,
                                   @FormParam("email") String email) {
        final User user = tokensPool.getUser(token);
        if (user == null) {
            return notFoundResponse("Invalid token. Please repeat all steps.");
        }
        final String hashedPassword = SHA256Util.makeHash(password, user.getEmail());
        resetPasswordController.invoke(token, hashedPassword, email);
        log.info("{} password was reset.", user.getEmail());
        tokensPool.removeToken(token);
        return Response.ok().build();
    }

    private Response notFoundResponse(String message) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(message)
                .build();
    }

}
