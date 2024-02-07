package com.senla.project.service.impl;

import com.senla.project.dto.request.ScoreRequest;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Score;
import com.senla.project.entity.User;
import com.senla.project.mapper.ScoreMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.ScoreRepository;
import com.senla.project.repository.UserRepository;
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
  private final UserRepository userRepository;
  private final ScoreRepository scoreRepository;

  private final ScoreMapper scoreMapper;


  @Transactional
  @Override
  public boolean setScoreToAd(long userId, long adId, ScoreRequest scoreRequest) {
    if (adRepository.existsById(adId)) {
      Score score = scoreMapper.mapToScore(scoreRequest);

      User setter = userRepository.findById(userId).get();
      score.setSetter(setter);

      Ad ad = adRepository.findById(adId).get();
      score.setAd(ad);
      score.setRating(ad.getSeller().getRating());

      scoreRepository.save(score);
      ratingService.updateRatingForUser(ad.getSeller().getId());

      ad.setScore(score);
      adRepository.save(ad);

      return true;
    }

    return false;
  }

}
