package com.senla.project.service.impl;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.User;
import com.senla.project.mapper.AdMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.RatingRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.AdService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
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
  private final RatingRepository ratingRepository;

  private final AdMapper adMapper;


  @Override
  public ResponseEntity<?> getFilteredAdsForUser(long userId, String searchString, String category, Integer minPrice, Integer maxPrice, Boolean isInMyCity) {
    List<Ad> ads = null;

    switch (category) {
      case ("open") -> ads = adRepository.findAllByNotSellerIdAndIsClosedFalse(userId);
      case ("current") -> ads = adRepository.findAllBySellerIdAndIsClosedFalse(userId);
      case ("closed") -> ads = adRepository.findAllBySellerIdAndIsClosedTrue(userId);
      case ("purchased") -> ads = adRepository.findAllByBuyerId(userId);
    }

    if (searchString != null) {
      ads = ads.stream()
          .filter(ad -> ad.getTitle().contains(searchString) || ad.getContent().contains(searchString))
          .collect(Collectors.toList());
    }

    if (minPrice != null) {
      ads = ads.stream()
          .filter(ad -> ad.getPrice() >= minPrice)
          .collect(Collectors.toList());
    }

    if (maxPrice != null) {
      ads = ads.stream()
          .filter(ad -> ad.getPrice() <= maxPrice)
          .collect(Collectors.toList());
    }

    if (category.equals("open") && isInMyCity != null) {
      User user = userRepository.findById(userId).get();

      if (isInMyCity) {
        ads = ads.stream()
            .filter(ad -> ad.getSeller().getAddress().equals(user.getAddress()))
            .collect(Collectors.toList());
      } else {
        ads = ads.stream()
            .filter(ad -> !ad.getSeller().getAddress().equals(user.getAddress()))
            .collect(Collectors.toList());
      }
    }

    if (category.equals("open")) {
      sortAds(ads);
    }

    
    List<?> adsResponses = null;

    switch (category) {
      case ("open") -> adsResponses = ads.stream()
          .map(adMapper::mapToAdOpenResponse)
          .collect(Collectors.toList());

      case ("current") -> adsResponses = ads.stream()
          .map(adMapper::mapToAdCurrentResponse)
          .collect(Collectors.toList());

      case ("closed") -> adsResponses = ads.stream()
          .map(adMapper::mapToAdClosedResponse)
          .collect(Collectors.toList());

      case ("purchased") -> adsResponses = ads.stream()
          .map(adMapper::mapToAdPurchasedResponse)
          .collect(Collectors.toList());
    }

    return ResponseEntity.ok(adsResponses);
  }

  @Override
  public ResponseEntity<?> getAd(long adId, long userId) {
    Ad ad = adRepository.findById(adId).get();

    if (Objects.equals(ad.getSeller().getId(), userId)) {
      if (ad.isClosed()) {
        return ResponseEntity.ok(adMapper.mapToAdClosedResponse(ad));
      } else {
        return ResponseEntity.ok(adMapper.mapToAdCurrentResponse(ad));
      }
    }

    if (Objects.equals(ad.getBuyer().getId(), userId)) {
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

  private void sortAds(List<Ad> ads) {
    Collections.sort(ads, Comparator
        .comparing((Ad ad) -> getRatingForSeller(ad.getSeller().getId()), Comparator.reverseOrder())
        .thenComparing(Ad::isPremium, Comparator.reverseOrder()));
  }

  private double getRatingForSeller(Long sellerId) {
    return ratingRepository.findByUserId(sellerId).get().getAverageScore();
  }

}
