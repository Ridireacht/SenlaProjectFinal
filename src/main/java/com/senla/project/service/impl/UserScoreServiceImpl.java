package com.senla.project.service.impl;

import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.request.UserScoreRequest;
import com.senla.project.entity.Ad;
import com.senla.project.entity.UserScore;
import com.senla.project.mapper.AdMapper;
import com.senla.project.mapper.UserScoreMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.service.UserScoreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserScoreServiceImpl implements UserScoreService {

  private final AdRepository adRepository;

  private final UserScoreMapper userScoreMapper;
  private final AdMapper adMapper;


  @Override
  public AdPurchasedResponse setUserScoreByAdId(Long userId, Long adId,
      UserScoreRequest userScoreRequest) {
    UserScore userScore = userScoreMapper.mapToUserScore(userScoreRequest);

    Ad ad = adRepository.findById(adId).get();
    ad.setScore(userScore);

    adRepository.save(ad);

    return adMapper.mapToAdPurchasedResponse(ad);
  }
}
