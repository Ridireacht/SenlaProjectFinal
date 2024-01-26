package com.senla.project.service;

import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdResponse;
import java.util.List;

public interface AdService {

  List<AdResponse> getAllAds();

  List<AdCurrentResponse> getCurrentAdsByUserId(Long userId);

  List<AdClosedResponse> getClosedAdsByUserId(Long userId);

  List<AdPurchasedResponse> getPurchasedAdsByUserId(Long userId);

  AdResponse getAdById(Long adId);

  AdResponse createAd(Long userId, AdRequest adRequest);

  AdResponse updateAd(Long adId, AdRequest adRequest);

  AdResponse makeAdPremium(Long adId);

  boolean deleteAd(Long adId);

  boolean doesAdExist(Long adId);

  boolean isAdAvailableForUser(Long adId, Long currentUserId);

  boolean doesAdBelongToUser(Long adId, Long currentUserId);

  boolean didUserBoughtAd(Long adId, Long currentUserId);
}
