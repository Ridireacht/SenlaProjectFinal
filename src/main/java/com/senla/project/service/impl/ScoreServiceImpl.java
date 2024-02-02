package com.senla.project.service.impl;

import com.senla.project.dto.request.ScoreRequest;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Score;
import com.senla.project.mapper.AdMapper;
import com.senla.project.mapper.ScoreMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.service.RatingService;
import com.senla.project.service.ScoreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ScoreServiceImpl implements ScoreService {

  private final RatingService ratingService;

  private final AdRepository adRepository;

  private final ScoreMapper scoreMapper;
  private final AdMapper adMapper;


  @Transactional
  @Override
  public boolean setScoreToAd(long userId, long adId, ScoreRequest scoreRequest) {
    if (adRepository.existsById(adId)) {
      Score score = scoreMapper.mapToScore(scoreRequest);

      Ad ad = adRepository.findById(adId).get();
      ad.setScore(score);

      ratingService.updateRatingForUser(score.getUser().getId());

      adRepository.save(ad);
    }

    return false;
  }

}
