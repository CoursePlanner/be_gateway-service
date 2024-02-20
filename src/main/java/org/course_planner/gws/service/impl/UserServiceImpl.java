package org.course_planner.gws.service.impl;

import org.course_planner.gws.constants.AuthenticationConstants;
import org.course_planner.gws.constants.UserConstants;
import org.course_planner.gws.dto.user.UserDetailsResponse;
import org.course_planner.gws.dto.user.UserProfileResponse;
import org.course_planner.gws.service.UserService;
import org.course_planner.utils.exceptions.PrerequisiteFailureException;
import org.course_planner.utils.exceptions.UserException;
import org.course_planner.utils.rest.GenericResponseTemplate;
import org.course_planner.utils.rest.RESTClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private RESTClient restClient;

    @Override
    public UserProfileResponse getUserProfile(String emailId, String userId) throws UserException {
        HttpHeaders headers = new HttpHeaders();
        if (userId != null) {
            headers.add(AuthenticationConstants.CONST_USER_ID_HEADER_NAME, userId);
        } else {
            headers.add(AuthenticationConstants.CONST_EMAIL_ID_HEADER_NAME, emailId);
        }
        GenericResponseTemplate<UserProfileResponse> httpResponse = restClient.execute(
                UserConstants.CONST_USER_SERVICE_PROP_REF, UserConstants.CONST_RETRIEVE_USER_PROP_REF,
                headers, null, null, null);
        UserProfileResponse userProfileResponse = httpResponse.getBody(UserProfileResponse.class);
        if (userProfileResponse == null) {
            throw new UserException("User does not exist!", HttpStatus.OK);
        }
        userProfileResponse.setPassword(null);
        return userProfileResponse;
    }

    @Override
    public UserDetailsResponse getUserProfileForAuth(String username) throws UserException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AuthenticationConstants.CONST_USERNAME_HEADER_NAME, username);
        GenericResponseTemplate<UserDetailsResponse> httpResponse = restClient.execute(
                UserConstants.CONST_USER_SERVICE_PROP_REF, UserConstants.CONST_RETRIEVE_USER_PROP_REF,
                headers, null, null, null);
        UserDetailsResponse userProfileResponse = httpResponse.getBody(UserDetailsResponse.class);
        if (userProfileResponse == null || userProfileResponse.getUserId() == null) {
            throw new UserException("User does not exist!", HttpStatus.FAILED_DEPENDENCY);
        }
        return userProfileResponse;
    }

    @Override
    public UserProfileResponse createProfile(UserProfileResponse userProfile) throws UserException, PrerequisiteFailureException {
        HttpHeaders headers = new HttpHeaders();
        userProfile.setUserId(null);
        userProfile.setAuthorities(new ArrayList<>());
        GenericResponseTemplate<UserProfileResponse> httpResponse = restClient.execute(
                UserConstants.CONST_USER_SERVICE_PROP_REF, UserConstants.CONST_CREATE_USER_PROP_REF,
                headers, userProfile, null, null);
        UserProfileResponse userProfileResponse = httpResponse.getBody(UserProfileResponse.class);
        if (userProfileResponse == null || userProfileResponse.getUserId() == null) {
            throw new UserException("User does not exist!", HttpStatus.FAILED_DEPENDENCY);
        }
        userProfileResponse.setPassword(null);
        return userProfileResponse;
    }

    @Override
    public UserProfileResponse updateUserProfile(String userId, UserProfileResponse userProfile) throws UserException, PrerequisiteFailureException {
        HttpHeaders headers = new HttpHeaders();
        GenericResponseTemplate<UserProfileResponse> httpResponse = restClient.execute(
                UserConstants.CONST_USER_SERVICE_PROP_REF, UserConstants.CONST_UPDATE_USER_PROP_REF,
                headers, userProfile, null, null);
        UserProfileResponse userProfileResponse = httpResponse.getBody(UserProfileResponse.class);
        if (userProfileResponse == null || userProfileResponse.getUserId() == null) {
            throw new UserException("User does not exist!", HttpStatus.FAILED_DEPENDENCY);
        }
        userProfileResponse.setPassword(null);
        return userProfileResponse;
    }

    @Override
    public UserProfileResponse deleteUserProfile(String userId) throws UserException {
        HttpHeaders headers = new HttpHeaders();
        List<String> pathParams = new ArrayList<>();
        pathParams.add(userId);
        GenericResponseTemplate<UserProfileResponse> httpResponse = restClient.execute(
                UserConstants.CONST_USER_SERVICE_PROP_REF, UserConstants.CONST_DELETE_USER_PROP_REF,
                headers, null, null, pathParams);
        UserProfileResponse userProfileResponse = httpResponse.getBody(UserProfileResponse.class);
        if (userProfileResponse == null || userProfileResponse.getUserId() == null) {
            throw new UserException("User does not exist!", HttpStatus.FAILED_DEPENDENCY);
        }
        userProfileResponse.setPassword(null);
        return userProfileResponse;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserProfileForAuth(username);
    }
}
