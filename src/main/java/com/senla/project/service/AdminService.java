package com.senla.project.service;

import com.senla.project.dto.response.UserProfileResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AdminService {

  List<UserProfileResponse> getAllUserProfiles();

  UserProfileResponse getUserProfileByUserId(Long userId);

  ResponseEntity<?> getFullAdInfoByAdId(Long adId);

  boolean removePremiumByAdId(Long adId);

  boolean deleteUserById(Long userId);
}
