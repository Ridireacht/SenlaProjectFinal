package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.senla.project.entity.Rating;
import com.senla.project.entity.Score;
import com.senla.project.repository.RatingRepository;
import com.senla.project.service.impl.RatingServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { RatingRepository.class, RatingServiceImpl.class })
@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

  @MockBean
  private RatingRepository ratingRepository;

  @Autowired
  private RatingService ratingService;


  @Test
  public void testUpdateRatingForUser() {
    long userId = 1L;

    Rating rating = createRating();
    List<Score> scores = createScores(3, 4, 5);
    rating.setScores(scores);

    when(ratingRepository.findByUserId(userId)).thenReturn(Optional.of(rating));
    when(ratingRepository.save(any(Rating.class))).thenAnswer(invocation -> invocation.getArgument(0));

    ratingService.updateRatingForUser(userId);

    double expectedAverageScore = (3 + 4 + 5) / 3.0;
    assertEquals(expectedAverageScore, rating.getAverageScore(), 0.01);
  }

  private Rating createRating() {
    Rating rating = new Rating();
    rating.setId(1L);
    rating.setAverageScore(2.5);
    return rating;
  }

  private List<Score> createScores(int... values) {
    List<Score> scores = new ArrayList<>();
    for (int value : values) {
      Score score = new Score();
      score.setValue(value);
      scores.add(score);
    }
    return scores;
  }
}
