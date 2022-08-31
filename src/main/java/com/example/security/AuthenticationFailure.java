package com.example.security;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class AuthenticationFailure implements AuthenticationFailureHandler{
    private final Gson gson;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Msg msg = null;
        if (e instanceof UsernameNotFoundException) {
            msg = Msg.fail(e.getMessage());
        } else if (e instanceof BadCredentialsException) {
            msg = Msg.fail("Password is wrong");
        } else {
            msg = Msg.fail(e.getMessage());
        }
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(gson.toJson(msg));
    }
}