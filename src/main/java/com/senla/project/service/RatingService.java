package com.senla.project.service;

import com.senla.project.dto.UserResponse;

public interface RatingService {

  UserResponse addScoreToRatingByUserId(Long userId, int score);
}
