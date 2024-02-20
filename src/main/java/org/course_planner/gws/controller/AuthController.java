package org.course_planner.gws.controller;

import org.course_planner.gws.dto.auth.LoginRequest;
import org.course_planner.gws.dto.auth.LoginResponse;
import org.course_planner.gws.service.AuthService;
import org.course_planner.utils.exceptions.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws AuthenticationException {
        if (loginRequest.getUsername().isBlank() || loginRequest.getPassword().isBlank()) {
            throw new AuthenticationException("Missing username/password!", HttpStatus.BAD_REQUEST);
        }
        return authService.login(loginRequest);
    }
}
