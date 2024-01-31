package com.senla.project.service;

import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AdminService {

  List<AdCurrentResponse> getCurrentAdsOfUser(Long userId);

  List<AdClosedResponse> getClosedAdsOfUser(Long userId);

  List<AdPurchasedResponse> getPurchasedAdsOfUser(Long userId);

  List<UserFullProfileResponse> getUserFullProfiles();

  UserFullProfileResponse getUserFullProfile(Long userId);

  ResponseEntity<?> getAd(Long adId);

  Long getAdId(Long commentId);

  boolean unmakeAdPremium(Long adId);

  boolean deleteUser(Long userId);

  boolean deleteComment(Long commentId);

  boolean deleteAd(Long adId);

  boolean doesUserExist(Long userId);

  boolean doesCommentExist(Long commentId);

  boolean doesAdExist(Long adId);

  boolean isAdClosed(Long adId);

  boolean isAdPremium(Long adId);
}
