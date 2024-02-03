package com.senla.project.service;

import com.senla.project.dto.request.UserProfileRequest;
import com.senla.project.dto.response.UserBriefProfileResponse;
import com.senla.project.dto.response.UserFullProfileResponse;

public interface UserService {

  UserBriefProfileResponse getUserBriefProfile(long userId);

  long getUserIdByUsername(String username);

  UserFullProfileResponse getUserFullProfile(long userId);

  boolean updateUserProfile(UserProfileRequest userProfileRequest, long userId);

  boolean doesUserExist(long userId);

  boolean doesUserExistByEmail(String email);

  boolean isUserBuyerOrSellerOfAd (long userId, long adId);
}
