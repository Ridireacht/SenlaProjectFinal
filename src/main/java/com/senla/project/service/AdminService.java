package com.senla.project.service;

import com.senla.project.dto.response.UserProfileResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AdminService {

  List<UserProfileResponse> getUserFullProfiles();

  UserProfileResponse getUserFullProfile(Long userId);

  ResponseEntity<?> getAd(Long adId);

  boolean unmakeAdPremium(Long adId);

  boolean deleteUser(Long userId);
}
