package ru.rstdv.monitoringservice.in.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.rstdv.monitoringservice.aspect.annotation.Loggable;

import java.io.IOException;
/**
 * сервлет ответственнен разлогирование пользователя
 * POST : /monitoring-service/logout
 */
@Loggable
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
