package com.senla.project.service;

import com.senla.project.dto.response.AdFullOpenResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AdminService {

  ResponseEntity<?> getFilteredAdsForUser(long userId, String searchString, String category, Integer minPrice, Integer maxPrice);

  List<AdFullOpenResponse> getAllOpenAdsFull(String searchString, Integer minPrice, Integer maxPrice);

  List<UserFullProfileResponse> getUserFullProfiles();

  UserFullProfileResponse getUserFullProfile(long userId);

  ResponseEntity<?> getAd(long adId);

  long getAdId(long commentId);

  boolean unmakeAdPremium(long adId);

  boolean deleteUser(long userId);

  boolean deleteComment(long commentId);

  boolean deleteAd(long adId);

  boolean doesUserExist(long userId);

  boolean doesCommentExist(long commentId);

  boolean doesAdExist(long adId);

  boolean isAdClosed(long adId);

  boolean isAdPremium(long adId);
}
