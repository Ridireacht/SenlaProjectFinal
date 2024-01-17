package com.senla.project.services.impl;

import com.senla.project.entities.Ad;
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
  public List<Ad> getAllAds() {
    return null;
  }

  @Override
  public List<Ad> getAdsByUserId(Long id) {
    return null;
  }

  @Override
  public Ad createAd() {
    return null;
  }

  @Override
  public Ad updateAd() {
    return null;
  }

  @Override
  public Ad makeAdPremium(Long id) {
    return null;
  }

  @Override
  public boolean deleteAd(Long id) {
    return false;
  }
}
