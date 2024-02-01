package com.senla.project.service.impl;

import com.senla.project.dto.request.UserScoreRequest;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.UserScore;
import com.senla.project.mapper.AdMapper;
import com.senla.project.mapper.UserScoreMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.service.RatingService;
import com.senla.project.service.UserScoreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserScoreServiceImpl implements UserScoreService {

  private final RatingService ratingService;

  private final AdRepository adRepository;

  private final UserScoreMapper userScoreMapper;
  private final AdMapper adMapper;


  @Transactional
  @Override
  public boolean setUserScoreToAd(long userId, long adId,
      UserScoreRequest userScoreRequest) {
    if (adRepository.existsById(adId)) {
      UserScore userScore = userScoreMapper.mapToUserScore(userScoreRequest);

      Ad ad = adRepository.findById(adId).get();
      ad.setScore(userScore);

      ratingService.updateRatingForUser(userScore.getUser().getId());

      adRepository.save(ad);
    }

    return false;
  }

}
