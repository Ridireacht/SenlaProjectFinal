package com.senla.project.services.impl;

import com.senla.project.dto.UserResponse;
import com.senla.project.repositories.RatingRepository;
import com.senla.project.services.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {

  private final RatingRepository ratingRepository;


  @Override
  public UserResponse addScoreToRatingByUserId(Long userId, int score) {
    return null;
  }
}
