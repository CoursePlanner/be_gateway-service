package org.course_planner.gws.service.impl;

import org.course_planner.gws.constants.AuthenticationConstants;
import org.course_planner.gws.dto.auth.AuthenticateRequestBody;
import org.course_planner.gws.dto.auth.AuthenticateResponseBody;
import org.course_planner.gws.dto.auth.LoginRequest;
import org.course_planner.gws.dto.auth.LoginResponse;
import org.course_planner.gws.dto.user.UserDetailsResponse;
import org.course_planner.gws.dto.user.UserProfileResponse;
import org.course_planner.gws.service.AuthService;
import org.course_planner.gws.service.UserService;
import org.course_planner.utils.exceptions.AuthenticationException;
import org.course_planner.utils.exceptions.UserException;
import org.course_planner.utils.rest.GenericResponseTemplate;
import org.course_planner.utils.rest.RESTClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RESTClient restClient;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        UserDetailsResponse userDetailsResponse = userService.getUserProfileForAuth(loginRequest.getUsername());
        AuthenticateRequestBody authenticateRequestBody = new AuthenticateRequestBody(new UserProfileResponse(userDetailsResponse));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, restClient.getBasicAuthenticationHeader(
                loginRequest.getUsername(), loginRequest.getPassword()));
        headers.add(AuthenticationConstants.CONST_USER_ID_HEADER_NAME, userDetailsResponse.getUserId() + "");

        GenericResponseTemplate<AuthenticateResponseBody> httpResponse = restClient.execute(
                AuthenticationConstants.CONST_AUTHENTICATION_SERVICE_PROP_REF, AuthenticationConstants.CONST_LOGIN_PROP_REF,
                headers, authenticateRequestBody, null, null);

        if (httpResponse.getHttpStatus().equals(HttpStatus.UNAUTHORIZED)) {
            throw new AuthenticationException("Username or password is incorrect!", HttpStatus.UNAUTHORIZED);
        }

        AuthenticateResponseBody authenticateResponseBody = null;
        try {
            authenticateResponseBody = httpResponse.getBody(AuthenticateResponseBody.class);
        } catch (Exception ex) {
            LOGGER.error("login: Failed to fetch data: {}", ex.getMessage());
        }

        LoginResponse loginResponse = new LoginResponse();
        if (authenticateResponseBody != null) {
            loginResponse.setStatus("Token generated!");
            loginResponse.setTokenExpiry(authenticateResponseBody.getExpiresAt());
            HttpHeaders responseHeaders = getAuthHeaders(authenticateResponseBody);
            return new ResponseEntity<>(loginResponse, responseHeaders, httpResponse.getHttpStatus());
        }

        loginResponse.setStatus("Token generation failed!");
        return new ResponseEntity<>(loginResponse, httpResponse.getHttpStatus());
    }

    private static HttpHeaders getAuthHeaders(AuthenticateResponseBody authenticateResponseBody) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(AuthenticationConstants.CONST_AUTH_TOKEN_HEADER_NAME, authenticateResponseBody.getToken());
        responseHeaders.add(AuthenticationConstants.CONST_USER_ID_HEADER_NAME, authenticateResponseBody.getUserId() + "");
        responseHeaders.add(AuthenticationConstants.CONST_USERNAME_HEADER_NAME, authenticateResponseBody.getUsername());
        responseHeaders.add(AuthenticationConstants.CONST_REFRESH_TOKEN_HEADER_NAME, authenticateResponseBody.getRefreshToken());
        return responseHeaders;
    }
}
