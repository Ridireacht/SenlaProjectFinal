package com.senla.project.service;

import com.senla.project.dto.request.UserScoreRequest;
import com.senla.project.dto.response.AdPurchasedResponse;

public interface UserScoreService {

  AdPurchasedResponse setUserScoreToAd(Long userId, Long adId, UserScoreRequest userScoreRequest);
}
