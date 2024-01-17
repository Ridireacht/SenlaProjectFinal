package com.senla.project.services;

import com.senla.project.entities.Ad;
import java.util.List;

public interface AdService {

  List<Ad> getAllAds();

  List<Ad> getAdsByUserId(Long id);

  Ad createAd();

  Ad updateAd();

  Ad makeAdPremium(Long id);

  boolean deleteAd(Long id);
}
