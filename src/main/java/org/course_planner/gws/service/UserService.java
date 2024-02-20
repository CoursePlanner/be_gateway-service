package org.course_planner.gws.service;

import org.course_planner.gws.dto.user.UserDetailsResponse;
import org.course_planner.gws.dto.user.UserProfileResponse;
import org.course_planner.utils.exceptions.UserException;

public interface UserService {
    UserProfileResponse getUserProfile(String emailId, String userId) throws UserException;

    UserDetailsResponse getUserProfileForAuth(String username) throws UserException;

    UserProfileResponse updateUserProfile(String userId, UserProfileResponse userProfile) throws UserException;

    UserProfileResponse createProfile(UserProfileResponse userProfile) throws UserException;

    UserProfileResponse deleteUserProfile(String userId) throws UserException;
}
