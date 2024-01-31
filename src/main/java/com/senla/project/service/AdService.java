package com.senla.project.service;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdOpenResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface AdService {

  List<AdOpenResponse> getOpenAdsFromOtherUsers(long userId);

  List<AdCurrentResponse> getCurrentAdsOfUser(long userId);

  List<AdClosedResponse> getClosedAdsOfUser(long userId);

  List<AdPurchasedResponse> getPurchasedAdsOfUser(long userId);

  ResponseEntity<?> getAd(long adId);

  AdCurrentResponse createAd(long userId, AdRequest adRequest);

  boolean updateAd(long adId, AdRequest adRequest);

  boolean makeAdPremium(long adId);

  boolean deleteAd(long adId);

  boolean doesAdExist(long adId);

  boolean doesAdBelongToUser(long adId, long userId);

  boolean isAdAvailableToUser(long adId, long userId);

  boolean isAdSoldToUser(long adId, long userId);

  boolean isAdScored(long adId);

  boolean isAdClosed(long adId);

  boolean isAdPremium(long adId);
}
