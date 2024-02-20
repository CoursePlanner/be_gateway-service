package org.course_planner.gws.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.course_planner.gws.constants.AuthenticationConstants;
import org.course_planner.gws.dto.auth.TokenValidationResponse;
import org.course_planner.gws.service.impl.UserServiceImpl;
import org.course_planner.utils.dto.ExceptionResponseDTO;
import org.course_planner.utils.exceptions.AuthorizationException;
import org.course_planner.utils.exceptions.GenericExceptionTemplate;
import org.course_planner.utils.exceptions.UnhandledException;
import org.course_planner.utils.rest.GenericResponseTemplate;
import org.course_planner.utils.rest.RESTClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
//@Order(value = Ordered.HIGHEST_PRECEDENCE)
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

        try {
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
        } catch (RuntimeException ex) {
            if (ex instanceof GenericExceptionTemplate) {
                writeCustomExceptionResponse(response, (GenericExceptionTemplate) ex);
                return;
            }
            writeCustomExceptionResponse(response, new UnhandledException(
                    ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getCause()));
        } catch (Exception ex) {
            writeCustomExceptionResponse(response, new UnhandledException(
                    ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getCause()));
        }
    }

    private void writeCustomExceptionResponse(HttpServletResponse response, GenericExceptionTemplate ex) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        response.setStatus(ex.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(new ExceptionResponseDTO(ex)));
        response.getWriter().flush();
        response.getWriter().close();
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

        TokenValidationResponse response = null;
        try {
            response = httpResponse.getBody(TokenValidationResponse.class);
        } catch (RuntimeException ex) {
            throw new AuthorizationException(ex.getMessage(), HttpStatus.FORBIDDEN, ex.getCause());
        }

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