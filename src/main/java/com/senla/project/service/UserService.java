package com.senla.project.service;

import com.senla.project.dto.request.UserProfileRequest;
import com.senla.project.dto.response.UserProfileResponse;
import com.senla.project.dto.response.UserResponse;

public interface UserService {

  UserResponse getUserBriefProfile(Long userId);

  Long getUserIdByUsername(String username);

  UserProfileResponse getUserFullProfile(Long userId);

  boolean updateUserProfile(UserProfileRequest userProfileRequest, Long userId);

  boolean doesUserExist(Long userId);

  boolean doesUserExistByEmail(String email);
}
