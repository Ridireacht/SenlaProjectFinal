package com.senla.project.service;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdOpenResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import java.util.List;

public interface AdService {

  List<AdOpenResponse> getOpenAdsFromOtherUsers(Long userId);

  List<AdCurrentResponse> getCurrentAdsOfUser(Long userId);

  List<AdClosedResponse> getClosedAdsOfUser(Long userId);

  List<AdPurchasedResponse> getPurchasedAdsOfUser(Long userId);

  AdOpenResponse getAd(Long adId);

  AdCurrentResponse createAd(Long userId, AdRequest adRequest);

  AdCurrentResponse updateAd(Long adId, AdRequest adRequest);

  Boolean makeAdPremium(Long adId);

  boolean deleteAd(Long adId);

  boolean doesAdExist(Long adId);

  boolean isAdAvailableToUser(Long adId, Long currentUserId);

  boolean doesAdBelongToUser(Long adId, Long currentUserId);

  boolean isAdSoldToUser(Long adId, Long currentUserId);

  boolean isAdScored(Long adId);

  boolean isAdClosed(Long adId);

  boolean isAdPremium(Long adId);
}
