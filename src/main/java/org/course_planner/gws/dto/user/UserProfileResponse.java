package org.course_planner.gws.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String userId;
    private String fullName;
    private String emailId;
    private String username;
    private String password;
    private Boolean enabled;
    private List<String> authorities;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public UserProfileResponse(UserDetailsResponse userDetailsResponse) {
        this.userId = userDetailsResponse.getUserId();
        this.fullName = userDetailsResponse.getFullName();
        this.emailId = userDetailsResponse.getEmailId();
        this.username = userDetailsResponse.getUsername();
        this.password = userDetailsResponse.getPassword();
        this.enabled = userDetailsResponse.getEnabled();
        this.authorities = userDetailsResponse.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        this.createdOn = userDetailsResponse.getCreatedOn();
        this.updatedOn = userDetailsResponse.getUpdatedOn();
    }
}
