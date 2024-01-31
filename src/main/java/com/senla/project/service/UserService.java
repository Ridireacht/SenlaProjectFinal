package com.senla.project.service;

import com.senla.project.dto.request.UserProfileRequest;
import com.senla.project.dto.response.UserProfileResponse;
import com.senla.project.dto.response.UserResponse;

public interface UserService {

  UserResponse getUserById(Long userId);

  Long getUserIdByUsername(String username);

  UserProfileResponse getUserProfileById(Long userId);

  boolean updateUserProfileById(UserProfileRequest userProfileRequest, Long userId);

  boolean doesUserExist(Long userId);

  boolean doesUserExistByEmail(String email);
}
