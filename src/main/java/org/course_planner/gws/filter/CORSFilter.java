package org.course_planner.gws.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.course_planner.gws.constants.AuthenticationConstants;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter extends OncePerRequestFilter {
    private static final String[] allowedHeaders = {
            AuthenticationConstants.CONST_AUTH_TOKEN_HEADER_NAME,
            AuthenticationConstants.CONST_REFRESH_TOKEN_HEADER_NAME,
            AuthenticationConstants.CONST_USERNAME_HEADER_NAME,
            AuthenticationConstants.CONST_USER_ID_HEADER_NAME,
            HttpHeaders.CONTENT_TYPE,
            HttpHeaders.ACCEPT
    };
    private static final String[] allowedHosts = {
            "http://localhost:3000"
    };

    private String getListAsStringForHeader(String[] params) {
        return Arrays.toString(params)
                .replace("[", "").replace("]", "");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, getListAsStringForHeader(allowedHosts));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, getListAsStringForHeader(allowedHeaders));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, getListAsStringForHeader(allowedHeaders));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "*");

        filterChain.doFilter(request, response);
    }
}
