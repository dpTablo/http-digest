package com.potalab.http.servlet;

import com.potalab.http.auth.digest.HttpDigestAuthorizationModule;
import com.potalab.http.auth.digest.HttpDigestConfiguration;
import com.potalab.http.auth.digest.exception.HttpDigestModuleRuntimeException;
import com.potalab.http.auth.digest.exception.NonceNotCreatedException;
import com.potalab.http.auth.digest.field.HttpDigestAlgorithm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "httpDigestServlet", value = "/digest")
public class HttpDigestServlet extends HttpServlet {
    private HttpDigestAuthorizationModule digestAuthorizationModule;

    @Override
    public void init() throws ServletException {
        HttpDigestConfiguration configuration = new HttpDigestConfiguration();
        configuration.setRealmName("r1");
        configuration.setNoncePrivateKey("np");
        configuration.setAlgorithm(HttpDigestAlgorithm.MD5_SESS);

        try {
            digestAuthorizationModule = new HttpDigestAuthorizationModule(configuration);
        } catch (HttpDigestModuleRuntimeException e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try {
            digestAuthorizationModule.authorize(request, response);
            if(response.getStatus() == HttpServletResponse.SC_OK) {
                response.setContentType("text/html; charset=UTF-8");
                response.getWriter().println("<h1>인증성공</h1");
                response.getWriter().println("<p>httpDigestServlet 내용 출력</p>");
            }
        } catch (NonceNotCreatedException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}
