package org.course_planner.gws.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateResponseBody {
    private String token;
    private String refreshToken;
    private String username;
    private String userId;
    private Long expiresAt;
}

