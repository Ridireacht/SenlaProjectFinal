package com.senla.project.service.impl;

import com.senla.project.dto.AdClosedResponse;
import com.senla.project.dto.AdCurrentResponse;
import com.senla.project.dto.AdPurchasedResponse;
import com.senla.project.dto.AdRequest;
import com.senla.project.dto.AdResponse;
import com.senla.project.repository.AdRepository;
import com.senla.project.service.AdService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdServiceImpl implements AdService {

  final AdRepository adRepository;


  @Override
  public List<AdResponse> getAllAds() {
    return null;
  }

  @Override
  public List<AdCurrentResponse> getCurrentAdsByUserId(Long userId) {
    return null;
  }

  @Override
  public List<AdClosedResponse> getClosedAdsByUserId(Long userId) {
    return null;
  }

  @Override
  public List<AdPurchasedResponse> getPurchasedAdsByUserId(Long userId) {
    return null;
  }

  @Override
  public AdResponse getAdById(Long adId) {
    return null;
  }

  @Override
  public AdResponse createAd(Long userId, AdRequest adRequest) {
    return null;
  }

  @Override
  public AdResponse updateAd(Long adId, AdRequest adRequest) {
    return null;
  }

  @Override
  public boolean setScoreForAd(Long adId, int score) {
    return false;
  }

  @Override
  public AdResponse makeAdPremium(Long adId) {
    return null;
  }

  @Override
  public AdResponse closeAd(Long adId, Long buyerId) {
    return null;
  }

  @Override
  public boolean deleteAd(Long adId) {
    return false;
  }

  @Override
  public boolean doesAdExist(Long adId) {
    return false;
  }

  @Override
  public boolean isAdAvailableForUser(Long adId, Long currentUserId) {
    return false;
  }

  @Override
  public boolean doesCommentBelongToUser(Long commentId, Long currentUserId) {
    return false;
  }

  @Override
  public boolean didUserBoughtAd(Long adId, Long currentUserId) {
    return false;
  }

}
