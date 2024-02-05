package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.senla.project.dto.request.ScoreRequest;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Rating;
import com.senla.project.entity.Score;
import com.senla.project.entity.User;
import com.senla.project.mapper.ScoreMapperImpl;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.ScoreRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.impl.RatingServiceImpl;
import com.senla.project.service.impl.ScoreServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { RatingServiceImpl.class, AdRepository.class, UserRepository.class, ScoreRepository.class, ScoreServiceImpl.class, ScoreMapperImpl.class })
@ExtendWith(MockitoExtension.class)
public class ScoreServiceTest {

  @MockBean
  private RatingService ratingService;

  @MockBean
  private AdRepository adRepository;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private ScoreRepository scoreRepository;

  @Autowired
  private ScoreService scoreService;


  @Test
  public void testSetScoreToAd() {
    long userId = 1L;
    long adId = 2L;

    ScoreRequest scoreRequest = createScoreRequest();
    User setter = createUser(userId);
    Ad ad = createAd(adId, userId);

    when(adRepository.existsById(adId)).thenReturn(true);
    when(userRepository.findById(userId)).thenReturn(Optional.of(setter));
    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
    when(scoreRepository.save(any(Score.class))).thenAnswer(invocation -> invocation.getArgument(0));

    boolean result = scoreService.setScoreToAd(userId, adId, scoreRequest);

    assertTrue(result);
  }

  private ScoreRequest createScoreRequest() {
    ScoreRequest scoreRequest = new ScoreRequest();
    scoreRequest.setValue(4);
    return scoreRequest;
  }

  private User createUser(long userId) {
    User user = new User();
    user.setId(userId);
    return user;
  }

  private Ad createAd(long adId, long userId) {
    Ad ad = new Ad();
    ad.setId(adId);
    ad.setSeller(createUser(userId));
    ad.getSeller().setRating(createRating());
    return ad;
  }

  private Rating createRating() {
    Rating rating = new Rating();
    rating.setId(1L);
    rating.setAverageScore(2.5);
    return rating;
  }
}
