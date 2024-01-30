package org.course_planner.gws.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course_planner.gws.dto.user.UserProfileResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateRequestBody {
    private UserProfileResponse userProfileDTO;
}
