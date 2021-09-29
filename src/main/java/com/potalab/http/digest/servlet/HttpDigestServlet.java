package com.potalab.http.digest.servlet;

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
    private static final Map<String, String> users = new HashMap<>();
    static {
        users.put("admin", "admin");
        users.put("user1", "1234");
        users.put("user2", "abcd");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Digest 인증으로 로그인하는 코드 작성
        response.getWriter().println("Welcome!");
    }
}
