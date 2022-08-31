package com.example.security;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccess implements AuthenticationSuccessHandler {
    @Autowired
    Gson gson;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Msg result=Msg.success("SUCCESS");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(gson.toJson(result));
    }
}