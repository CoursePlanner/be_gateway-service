package org.course_planner.gws.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationResponse {
    private String principal;
    private List<String> authorities;
    private Long expiresAt;
    private Long issuedAt;
    private String issuer;
}
