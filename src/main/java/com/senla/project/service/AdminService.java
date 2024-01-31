package com.senla.project.service;

import com.senla.project.dto.response.UserFullProfileResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AdminService {

  List<UserFullProfileResponse> getUserFullProfiles();

  UserFullProfileResponse getUserFullProfile(Long userId);

  ResponseEntity<?> getAd(Long adId);

  boolean unmakeAdPremium(Long adId);

  boolean deleteUser(Long userId);
}
