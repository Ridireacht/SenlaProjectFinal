package com.senla.project.services;

import com.senla.project.dto.AdRequest;
import com.senla.project.dto.AdResponse;
import java.util.List;

public interface AdService {

  List<AdResponse> getAllAds();

  List<AdResponse> getCurrentAdsByUserId(Long id);

  List<AdResponse> getClosedAdsByUserId(Long id);

  AdResponse getAdById(Long id);

  AdResponse createAd(AdRequest adRequest);

  AdResponse updateAd(AdRequest adRequest);

  AdResponse makeAdPremium(Long id);

  boolean deleteAd(Long id);
}
