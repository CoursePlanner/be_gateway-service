package org.course_planner.gws.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.course_planner.gws.constants.AuthenticationConstants;
import org.course_planner.gws.dto.auth.TokenValidationResponse;
import org.course_planner.gws.service.impl.UserServiceImpl;
import org.course_planner.utils.exceptions.AuthorizationException;
import org.course_planner.utils.rest.GenericResponseTemplate;
import org.course_planner.utils.rest.RESTClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserServiceImpl userDetailsService;

    @Autowired
    private RESTClient restClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        String username = getUsernameFromRequest(request);
        if (request.getRequestURI().toLowerCase().endsWith("/message/receive")) {
            token = getTokenFromUrl(request);
            username = getUserFromUrl(request);
        }

        boolean isValidToken = isValidToken(token, username);
        if (isValidToken) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } else {
            throw new AuthorizationException("You are not authorized to access this resource!", HttpStatus.FORBIDDEN);
        }
    }

    private String getTokenFromUrl(HttpServletRequest request) {
        String[] values = request.getParameterValues("token");
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    private String getUserFromUrl(HttpServletRequest request) {
        String[] values = request.getParameterValues("username");
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    private boolean isValidToken(String token, String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        headers.add(AuthenticationConstants.CONST_USER_ID_HEADER_NAME, username);

        GenericResponseTemplate<TokenValidationResponse> httpResponse = restClient.execute(
                AuthenticationConstants.CONST_AUTHENTICATION_SERVICE_PROP_REF, AuthenticationConstants.CONST_VALIDATE_PROP_REF,
                headers, null, null, null);

        TokenValidationResponse response = httpResponse.getBody(TokenValidationResponse.class);

        return response != null && response.getPrincipal() != null && response.getPrincipal().equals(username);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AuthenticationConstants.CONST_AUTH_TOKEN_HEADER_NAME);
        if (bearerToken != null && !bearerToken.isBlank()) {
            return bearerToken;
        } else {
            return null;
        }
    }

    private String getUsernameFromRequest(HttpServletRequest request) {
        String username = request.getHeader(AuthenticationConstants.CONST_USERNAME_HEADER_NAME);
        if (username != null && !username.isBlank()) {
            return username;
        } else {
            return null;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().toLowerCase().contains("/auth/login")
                || request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.toString());
    }
}