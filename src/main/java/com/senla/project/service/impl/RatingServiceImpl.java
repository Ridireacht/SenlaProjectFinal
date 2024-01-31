package com.senla.project.service.impl;

import com.senla.project.entity.Rating;
import com.senla.project.entity.UserScore;
import com.senla.project.repository.RatingRepository;
import com.senla.project.service.RatingService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {

  private final RatingRepository ratingRepository;

  @Transactional
  @Override
  public void updateRatingForUser(long userId) {
    Rating rating = ratingRepository.findByUserId(userId).get();
    List<UserScore> userRatings = rating.getUserRatings();

    double sumOfScores = userRatings.stream()
        .mapToDouble(UserScore::getScore)
        .sum();

    double averageScore = sumOfScores / userRatings.size();
    rating.setAverageScore(averageScore);

    ratingRepository.save(rating);
  }
}
