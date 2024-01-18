package com.senla.project.services;

import com.senla.project.dto.AdPurchasedResponse;

public interface UserScoreService {

  AdPurchasedResponse setUserScoreByAdId(Long id);
}
