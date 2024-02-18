package ru.rstdv.monitoringservice.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.rstdv.monitoringservice.dto.read.ReadUserDto;
import ru.rstdv.monitoringservice.entity.embeddable.Role;

import java.io.IOException;
import java.util.Set;

import static ru.rstdv.monitoringservice.util.UrlPath.*;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {

    private static final Set<String> PUBLIC_PATH = Set.of(AUTHENTICATION, REGISTRATION, LOGOUT);
    private static final Set<String> PUBLIC_PATH_IF_AUTHENTICATED = Set.of(SEND_WATER_METER_READING, SEND_THERMAL_METER_READING);
    private static final String USER_ATTRIBUTE = "user";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var uri = ((HttpServletRequest) request).getRequestURI();
        if (isPublicPath(uri)) {
            chain.doFilter(request, response);
        } else {
            response.setContentType("application/json");
            if (!isUserAuthenticated(request)) {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("go to registration or authentication");
            } else if (isUserAuthenticated(request) && isPublicPathForAuthenticatedUser(uri)) {
                chain.doFilter(request, response);
            } else if (isUserAuthenticated(request) && hasPermissions(request)) {
                chain.doFilter(request, response);
            } else {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("you do not have access to this url");
            }
        }
    }

    private boolean isUserAuthenticated(ServletRequest request) {
        var readUserDto = (ReadUserDto) ((HttpServletRequest) request).getSession().getAttribute(USER_ATTRIBUTE);
        return readUserDto != null;
    }

    private boolean hasPermissions(ServletRequest request) {
        var readUserDto = (ReadUserDto) ((HttpServletRequest) request).getSession().getAttribute(USER_ATTRIBUTE);
        if (readUserDto.role().equals(Role.ADMIN.name())) {
            return true;
        }
        if (!((HttpServletRequest) request).getRequestURI().replaceAll("/monitoring-service/api/v1\\D+", "").isEmpty()) {
            var id = ((HttpServletRequest) request).getRequestURI().replaceAll("/monitoring-service/api/v1\\D+", "");
            if (((ReadUserDto) ((HttpServletRequest) request).getSession().getAttribute(USER_ATTRIBUTE)).id().equals(id))
                return true;
        }
        if (request.getParameter("userId") != null) {
            return request.getParameter("userId").equals(readUserDto.id());
        }
        return false;
    }

    private boolean isPublicPath(String uri) {
        return PUBLIC_PATH.stream()
                .anyMatch(uri::startsWith);
    }

    private boolean isPublicPathForAuthenticatedUser(String uri) {
        return PUBLIC_PATH_IF_AUTHENTICATED.stream()
                .anyMatch(uri::startsWith);
    }
}
