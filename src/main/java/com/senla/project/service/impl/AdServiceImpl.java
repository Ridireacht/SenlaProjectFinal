package com.senla.project.service.impl;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdOpenResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.User;
import com.senla.project.mapper.AdMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.AdService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AdServiceImpl implements AdService {

  private final AdRepository adRepository;
  private final UserRepository userRepository;

  private final AdMapper adMapper;


  @Override
  public ResponseEntity<?> getFilteredAdsForUser(long userId, String searchString, String category, Integer minPrice, Integer maxPrice, Boolean isInMyCity) {
    List<Ad> ads = adRepository.findAllByNotSellerIdAndIsClosedFalse(userId);
    return ads.stream()
        .map(adMapper::mapToAdOpenResponse)
        .collect(Collectors.toList());
  }

  @Override
  public ResponseEntity<?> getAd(long adId, long userId) {
    Ad ad = adRepository.findById(adId).get();
    User user = userRepository.findById(userId).get();

    if (Objects.equals(ad.getSeller().getId(), user.getId())) {
      if (ad.isClosed()) {
        return ResponseEntity.ok(adMapper.mapToAdClosedResponse(ad));
      } else {
        return ResponseEntity.ok(adMapper.mapToAdCurrentResponse(ad));
      }
    }

    if (Objects.equals(ad.getBuyer().getId(), user.getId())) {
      return ResponseEntity.ok(adMapper.mapToAdPurchasedResponse(ad));
    }

    return ResponseEntity.ok(adMapper.mapToAdOpenResponse(ad));
  }

  @Transactional
  @Override
  public AdCurrentResponse createAd(long userId, AdRequest adRequest) {
    User seller = userRepository.findById(userId).get();

    Ad ad = adMapper.mapToAd(adRequest);
    ad.setSeller(seller);
    ad.setPostedAt(LocalDateTime.now());

    Ad savedAd = adRepository.save(ad);
    return adMapper.mapToAdCurrentResponse(savedAd);
  }

  @Transactional
  @Override
  public boolean updateAd(long adId, AdRequest adRequest) {
    if (adRepository.existsById(adId)) {
      Ad ad = adRepository.findById(adId).get();

      ad.setTitle(adRequest.getTitle());
      ad.setContent(adRequest.getContent());
      ad.setPrice(adRequest.getPrice());
      ad.setPostedAt(LocalDateTime.now());

      adRepository.save(ad);
      return true;
    }

    return false;
  }

  @Transactional
  @Override
  public boolean makeAdPremium(long adId) {
    if (adRepository.existsById(adId)) {
      Ad ad = adRepository.findById(adId).get();

      ad.setPremium(true);
      adRepository.save(ad);

      return true;
    }

    return false;
  }

  @Transactional
  @Override
  public boolean deleteAd(long adId) {
    if (adRepository.existsById(adId)) {
      adRepository.deleteById(adId);
      return true;
    }

    return false;
  }

  @Override
  public boolean doesAdExist(long adId) {
    return adRepository.existsById(adId);
  }

  @Override
  public boolean doesAdBelongToUser(long adId, long userId) {
    Ad ad = adRepository.findById(adId).get();
    return ad.getSeller().getId() == userId;
  }

  @Override
  public boolean isAdSoldToUser(long adId, long userId) {
    Ad ad = adRepository.findById(adId).get();
    return ad.getBuyer().getId() == userId;
  }

  @Override
  public boolean isAdScored(long adId) {
    Ad ad = adRepository.findById(adId).get();
    return ad.getScore() != null;
  }

  @Override
  public boolean isAdClosed(long adId) {
    Ad ad = adRepository.findById(adId).get();
    return ad.isClosed();
  }

  @Override
  public boolean isAdPremium(long adId) {
    Ad ad = adRepository.findById(adId).get();
    return ad.isPremium();
  }

}
