package com.potalab.http.digest.servlet;

import com.potalab.http.digest.auth.HttpDigestAuthorizationModule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "httpDigestServlet", value = "/digest")
public class HttpDigestServlet extends HttpServlet {
    private HttpDigestAuthorizationModule digestAuthorizationModule = new HttpDigestAuthorizationModule("testingRealm");

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        digestAuthorizationModule.authorize(request, response);
    }
}
