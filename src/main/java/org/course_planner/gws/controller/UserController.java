package org.course_planner.gws.controller;

import org.course_planner.gws.dto.user.UserProfileResponse;
import org.course_planner.gws.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileResponse> createUser(@RequestBody UserProfileResponse user) {
        if (user.getUserId() != null) {
            return null;
        }
        return new ResponseEntity<>(userService.createProfile(user), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/updateUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileResponse> updateUser(@RequestHeader("x-user-id") String userId, @RequestBody UserProfileResponse user) {
        return new ResponseEntity<>(userService.updateUserProfile(userId, user), HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileResponse> getUser(@RequestHeader("x-user-id") String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }
        return new ResponseEntity<>(userService.getUserProfile(null, userId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfileResponse> updateUser(@RequestHeader("x-user-id") String userId) {
        return new ResponseEntity<>(userService.deleteUserProfile(userId), HttpStatus.OK);
    }
}
