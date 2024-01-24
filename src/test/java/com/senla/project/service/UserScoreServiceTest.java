package com.senla.project.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.senla.project.dto.request.UserScoreRequest;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.response.UserResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.User;
import com.senla.project.repository.AdRepository;
import java.util.Arrays;
import java.util.List;
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
    doNothing().when(adRepository.save(ad));

    AdPurchasedResponse result = userScoreService.setUserScoreByAdId(userId, adId, userScoreRequest);

    assertEquals(result.getId(), adId);
    assertEquals(result.getScore(), score);
  }
}
