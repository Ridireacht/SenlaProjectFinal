package com.senla.project.services.impl;

import com.senla.project.dto.AdRequest;
import com.senla.project.dto.AdResponse;
import com.senla.project.repositories.AdRepository;
import com.senla.project.services.AdService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdServiceImpl implements AdService {

  final AdRepository adRepository;


  @Override
  public List<AdResponse> getAllAds() {
    return null;
  }

  @Override
  public List<AdResponse> getAdsByUserId(Long id) {
    return null;
  }

  @Override
  public AdResponse getAdById(Long id) {
    return null;
  }

  @Override
  public AdResponse createAd(AdRequest adRequest) {
    return null;
  }

  @Override
  public AdResponse updateAd(AdRequest adRequest) {
    return null;
  }

  @Override
  public AdResponse makeAdPremium(Long id) {
    return null;
  }

  @Override
  public boolean deleteAd(Long id) {
    return false;
  }
}
