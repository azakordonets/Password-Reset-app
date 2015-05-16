package com.azakordonets.web.handlers;

import com.azakordonets.utils.SHA256Util;
import com.azakordonets.web.controller.ResetPasswordController;
import com.azakordonets.web.entities.TokensPool;
import com.azakordonets.web.entities.User;
import fabricator.Fabricator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.URISyntaxException;

@Path("/")
public class ResetPasswordHandler {

    private final ResetPasswordController resetPasswordController;
    private final TokensPool tokensPool;

    public ResetPasswordHandler(String url, int port, TokensPool tokensPool) throws Exception {
        this.resetPasswordController = new ResetPasswordController(url, port, tokensPool);
        this.tokensPool = tokensPool;
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_FORM_URLENCODED)
    @Path("resetPassword")
    public Response sendResetPasswordEmail(MultivaluedMap<String, String> params) {
        if (!params.containsKey("user")) {
            return badRequestResponse("User param is missing");
        }
        final String email = params.getFirst("user");
        final String token = Fabricator.alphaNumeric().hash(60);
        if (email == null || email.isEmpty()) {
            return badRequestResponse("Email cannot be empty");
        } else {
            resetPasswordController.sendResetPasswordEmail(email, token);
            return Response.ok().entity("Email was sent").build();
        }
    }


    private Response badRequestResponse(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(message)
                .build();
    }

    @GET
    @Produces(value = MediaType.TEXT_HTML)
    @Path("landing")
    public Response test(@QueryParam("token") String token) throws URISyntaxException {
        if (tokensPool.tokenExists(token)){
            File page = resetPasswordController.getResetPasswordPage();
            return Response.ok(page).build();

        } else {
            return notFoundResponse(String.format("%s token was not found or it is outdated", token));
        }
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
            return notFoundResponse(String.format("User with token=%s was not found", token));
        }
        final String hashedPassword = SHA256Util.makeHash(password, user.getEmail());
        resetPasswordController.invoke(token, hashedPassword, email);
        tokensPool.removeToken(token);
        return Response.ok().build();
    }

    private Response notFoundResponse(String message) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(message)
                .build();
    }

}
