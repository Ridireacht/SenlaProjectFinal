package com.senla.project.services;

import com.senla.project.dto.AdRequest;
import com.senla.project.dto.AdResponse;
import java.util.List;

public interface AdService {

  List<AdResponse> getAllAds();

  List<AdResponse> getCurrentAdsByUserId(Long userId);

  List<AdResponse> getClosedAdsByUserId(Long userId);

  List<AdResponse> getPurchasedAdsByUserId(Long userId);

  AdResponse getAdById(Long id);

  AdResponse createAd(AdRequest adRequest);

  AdResponse updateAd(AdRequest adRequest);

  AdResponse makeAdPremium(Long id);

  AdResponse closeAd(Long adId, Long buyerId);

  boolean deleteAd(Long id);
}
