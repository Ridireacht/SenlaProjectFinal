package com.senla.project.service;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdCurrentResponse;
import org.springframework.http.ResponseEntity;

public interface AdService {

  ResponseEntity<?> getFilteredAdsForUser(long userId, String searchString, String category, Integer minPrice, Integer maxPrice, Boolean isInMyCity);

  ResponseEntity<?> getAd(long adId, long userId);

  AdCurrentResponse createAd(long userId, AdRequest adRequest);

  boolean updateAd(long adId, AdRequest adRequest);

  boolean makeAdPremium(long adId);

  boolean deleteAd(long adId);

  boolean doesAdExist(long adId);

  boolean doesAdBelongToUser(long adId, long userId);

  boolean isAdSoldToUser(long adId, long userId);

  boolean isAdScored(long adId);

  boolean isAdClosed(long adId);

  boolean isAdPremium(long adId);
}
