package com.senla.project.service;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdOpenResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import java.util.List;

public interface AdService {

  List<AdOpenResponse> getOpenAdsFromOtherUsers(long userId);

  List<AdCurrentResponse> getCurrentAdsOfUser(long userId);

  List<AdClosedResponse> getClosedAdsOfUser(long userId);

  List<AdPurchasedResponse> getPurchasedAdsOfUser(long userId);

  AdOpenResponse getAd(long adId);

  AdCurrentResponse createAd(long userId, AdRequest adRequest);

  AdCurrentResponse updateAd(long adId, AdRequest adRequest);

  Boolean makeAdPremium(long adId);

  boolean deleteAd(long adId);

  boolean doesAdExist(long adId);

  boolean isAdAvailableToUser(long adId, long currentUserId);

  boolean doesAdBelongToUser(long adId, long currentUserId);

  boolean isAdSoldToUser(long adId, long currentUserId);

  boolean isAdScored(long adId);

  boolean isAdClosed(long adId);

  boolean isAdPremium(long adId);
}
