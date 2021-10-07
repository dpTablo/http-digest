package com.potalab.http.servlet;

import com.potalab.http.auth.digest.HttpDigestAuthorizationModule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "httpDigestServlet", value = "/digest")
public class HttpDigestServlet extends HttpServlet {
    private HttpDigestAuthorizationModule digestAuthorizationModule = new HttpDigestAuthorizationModule("testingRealm");

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {

        digestAuthorizationModule.authorize(request, response);

        if(response.getStatus() == HttpServletResponse.SC_OK) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println("<h1>인증성공</h1");
            response.getWriter().println("<p>httpDigestServlet 내용 출력</p>");
        }
    }
}
