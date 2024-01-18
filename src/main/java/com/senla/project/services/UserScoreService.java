package com.senla.project.services;

import com.senla.project.dto.AdPurchasedResponse;
import com.senla.project.dto.UserScoreRequest;

public interface UserScoreService {

  AdPurchasedResponse setUserScoreByAdId(Long id, UserScoreRequest userScoreRequest);
}