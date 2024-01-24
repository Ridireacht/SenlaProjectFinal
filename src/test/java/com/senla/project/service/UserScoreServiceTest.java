package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.senla.project.dto.request.UserScoreRequest;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.entity.Ad;
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

    when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
    when(adRepository.save(ad)).thenReturn(ad);

    AdPurchasedResponse result = userScoreService.setUserScoreByAdId(userId, adId, userScoreRequest);

    assertEquals(adId, result.getId());
    assertEquals(score, result.getScore());
  }
}
