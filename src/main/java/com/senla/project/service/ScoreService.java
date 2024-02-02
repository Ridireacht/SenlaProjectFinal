package com.senla.project.service;

import com.senla.project.dto.request.ScoreRequest;

public interface ScoreService {

  boolean setScoreToAd(long userId, long adId, ScoreRequest scoreRequest);
}
