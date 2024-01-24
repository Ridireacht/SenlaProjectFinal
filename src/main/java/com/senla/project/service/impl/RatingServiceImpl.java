package com.senla.project.service.impl;

import com.senla.project.dto.response.UserResponse;
import com.senla.project.repository.RatingRepository;
import com.senla.project.service.RatingService;
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
