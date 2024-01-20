package com.senla.project.services;

import com.senla.project.dto.AdClosedResponse;
import com.senla.project.dto.AdCurrentResponse;
import com.senla.project.dto.AdPurchasedResponse;
import com.senla.project.dto.AdRequest;
import com.senla.project.dto.AdResponse;
import java.util.List;

public interface AdService {

  List<AdResponse> getAllAds();

  List<AdCurrentResponse> getCurrentAdsByUserId(Long userId);

  List<AdClosedResponse> getClosedAdsByUserId(Long userId);

  List<AdPurchasedResponse> getPurchasedAdsByUserId(Long userId);

  AdResponse getAdById(Long adId);

  AdResponse createAd(Long userId, AdRequest adRequest);

  AdResponse updateAd(Long adId, AdRequest adRequest);

  boolean setScoreForAd(Long adId, int score);

  AdResponse makeAdPremium(Long adId);

  AdResponse closeAd(Long adId, Long buyerId);

  boolean deleteAd(Long adId);
}
