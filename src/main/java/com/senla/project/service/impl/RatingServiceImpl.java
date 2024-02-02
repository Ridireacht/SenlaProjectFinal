package com.senla.project.service.impl;

import com.senla.project.entity.Rating;
import com.senla.project.entity.Score;
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
    List<Score> scores = rating.getScores();

    double sumOfScores = scores.stream()
        .mapToDouble(Score::getValue)
        .sum();

    double averageScore = sumOfScores / scores.size();
    rating.setAverageScore(averageScore);

    ratingRepository.save(rating);
  }
}
