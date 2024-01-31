package com.senla.project.service;

import com.senla.project.dto.request.UserProfileRequest;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.dto.response.UserBriefProfileResponse;

public interface UserService {

  UserBriefProfileResponse getUserBriefProfile(Long userId);

  Long getUserIdByUsername(String username);

  UserFullProfileResponse getUserFullProfile(Long userId);

  boolean updateUserProfile(UserProfileRequest userProfileRequest, Long userId);

  boolean doesUserExist(Long userId);

  boolean doesUserExistByEmail(String email);
}
