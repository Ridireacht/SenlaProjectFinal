package com.senla.project.service.impl;

import com.senla.project.dto.AdPurchasedResponse;
import com.senla.project.dto.UserScoreRequest;
import com.senla.project.repository.UserScoreRepository;
import com.senla.project.service.UserScoreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserScoreServiceImpl implements UserScoreService {

  private final UserScoreRepository userScoreRepository;


  @Override
  public AdPurchasedResponse setUserScoreByAdId(Long userId, Long adId,
      UserScoreRequest userScoreRequest) {
    return null;
  }
}
