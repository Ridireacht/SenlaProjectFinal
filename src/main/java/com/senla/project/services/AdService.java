package com.senla.project.services;

import com.senla.project.entities.Ad;
import java.util.List;

public interface AdService {

  List<Ad> getAllAds();

  List<Ad> getAdsByUserId();

  Ad createAd();

  Ad updateAd();

  Ad makeAdPremium();

  boolean deleteAd();
}
