package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.senla.project.dto.request.UserScoreRequest;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.UserScore;
import com.senla.project.mapper.AdMapper;
import com.senla.project.mapper.UserScoreMapper;
import com.senla.project.repository.AdRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserScoreServiceTest {

  @MockBean
  AdRepository adRepository;

  @MockBean
  UserScoreMapper userScoreMapper;

  @MockBean
  AdMapper adMapper;

  @Autowired
  UserScoreService userScoreService;


  @Test
  void testSetUserScoreByAdId() {
    int score = 3;
    Long userId = 1L;
    Long adId = 2L;

    Ad ad = new Ad();
    ad.setId(adId);

    UserScoreRequest userScoreRequest = new UserScoreRequest();
    userScoreRequest.setScore(score);

    UserScore expectedUserScore = new UserScore();
    expectedUserScore.setScore(score);

    AdPurchasedResponse expectedAdPurchasedResponse = new AdPurchasedResponse();
    expectedAdPurchasedResponse.setId(adId);
    expectedAdPurchasedResponse.setScore(score);

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
    when(adRepository.save(ad)).thenReturn(ad);

    when(userScoreMapper.mapToUserScore(userScoreRequest)).thenReturn(expectedUserScore);
    when(adMapper.mapToAdPurchasedResponse(ad)).thenReturn(expectedAdPurchasedResponse);

    AdPurchasedResponse actualAdPurchasedResponse = userScoreService.setUserScoreByAdId(userId, adId, userScoreRequest);

    assertEquals(adId, actualAdPurchasedResponse.getId());
    assertEquals(score, actualAdPurchasedResponse.getScore());
  }
}
