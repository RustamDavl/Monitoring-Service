package ru.rstdv.monitoringservice.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        try (var writer = resp.getWriter()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            writer.write("redirecting to login page...");
            writer.flush();
        }
    }
}
