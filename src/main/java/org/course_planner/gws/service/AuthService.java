package org.course_planner.gws.service;

import org.course_planner.gws.dto.auth.LoginRequest;
import org.course_planner.gws.dto.auth.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<LoginResponse> login(LoginRequest loginRequest);
}
