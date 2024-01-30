package com.senla.project.service;

import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdResponse;
import java.util.List;

public interface AdService {

  List<AdResponse> getAllAdsFromOthers(Long userId);

  List<AdCurrentResponse> getCurrentAdsByUserId(Long userId);

  List<AdClosedResponse> getClosedAdsByUserId(Long userId);

  List<AdPurchasedResponse> getPurchasedAdsByUserId(Long userId);

  AdResponse getAdById(Long adId);

  AdCurrentResponse createAd(Long userId, AdRequest adRequest);

  AdCurrentResponse updateAd(Long adId, AdRequest adRequest);

  AdCurrentResponse makeAdPremium(Long adId);

  boolean deleteAd(Long adId);

  boolean doesAdExist(Long adId);

  boolean isAdAvailableForUser(Long adId, Long currentUserId);

  boolean doesAdBelongToUser(Long adId, Long currentUserId);

  boolean didUserBoughtAd(Long adId, Long currentUserId);

  boolean isAdAlreadyScored(Long adId);

  boolean isAdClosed(Long adId);

  boolean isAdPremium(Long adId);
}
