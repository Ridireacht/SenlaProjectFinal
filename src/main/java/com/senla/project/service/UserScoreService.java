package com.senla.project.service;

import com.senla.project.dto.request.UserScoreRequest;

public interface UserScoreService {

  boolean setUserScoreToAd(long userId, long adId, UserScoreRequest userScoreRequest);
}
