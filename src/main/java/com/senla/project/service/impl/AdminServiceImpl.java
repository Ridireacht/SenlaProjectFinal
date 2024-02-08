package com.senla.project.service.impl;

import com.senla.project.dto.response.AdFullOpenResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Comment;
import com.senla.project.entity.User;
import com.senla.project.mapper.AdMapper;
import com.senla.project.mapper.UserMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.CommentRepository;
import com.senla.project.repository.RatingRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.AdminService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;
  private final AdRepository adRepository;
  private final CommentRepository commentRepository;
  private final RatingRepository ratingRepository;

  private final UserMapper userMapper;
  private final AdMapper adMapper;


  @Override
  public ResponseEntity<?> getFilteredAdsForUser(long userId, String searchString, String category, Integer minPrice, Integer maxPrice) {
    List<Ad> ads = null;

    switch (category) {
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


    List<?> adsResponses = null;

    switch (category) {
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
  public List<AdFullOpenResponse> getAllOpenAdsFull(String searchString, Integer minPrice,
      Integer maxPrice) {

    List<Ad> ads = adRepository.findAllByIsClosedFalse();

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

    sortAds(ads);

    List<AdFullOpenResponse> adsResponses = ads.stream()
        .map(adMapper::mapToAdFullOpenResponse)
        .collect(Collectors.toList());

    return adsResponses;
  }

  @Override
  public List<UserFullProfileResponse> getUserFullProfiles() {
    List<User> users = userRepository.findAll();
    return users.stream()
        .map(userMapper::mapToUserFullProfileResponse)
        .collect(Collectors.toList());
  }

  @Override
  public UserFullProfileResponse getUserFullProfile(long userId) {
    User user = userRepository.findById(userId).get();
    return userMapper.mapToUserFullProfileResponse(user);
  }

  @Override
  public ResponseEntity<?> getAd(long adId) {
    Ad ad = adRepository.findById(adId).get();

    if (ad.isClosed()) {
      return ResponseEntity.ok(adMapper.mapToAdClosedResponse(ad));
    } else {
      return ResponseEntity.ok(adMapper.mapToAdCurrentResponse(ad));
    }
  }

  @Override
  public long getAdId(long commentId) {
    Comment comment = commentRepository.findById(commentId).get();
    return comment.getAd().getId();
  }

  @Transactional
  @Override
  public boolean unmakeAdPremium(long adId) {
    Ad ad = adRepository.findById(adId).get();

    ad.setPremium(false);
    adRepository.save(ad);

    ad = adRepository.findById(adId).get();

    return !ad.isPremium();
  }

  @Transactional
  @Override
  public boolean deleteUser(long userId) {
    if (userRepository.existsById(userId)) {
      userRepository.deleteById(userId);
      adRepository.deleteAllBySellerIdAndIsClosedFalse(userId);
      return true;
    }

    return false;
  }

  @Transactional
  @Override
  public boolean deleteComment(long commentId) {
    if (commentRepository.existsById(commentId)) {
      commentRepository.deleteById(commentId);
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
  public boolean doesUserExist(long userId) {
    return userRepository.existsById(userId);
  }

  @Override
  public boolean doesCommentExist(long commentId) {
    return commentRepository.existsById(commentId);
  }

  @Override
  public boolean doesAdExist(long adId) {
    return adRepository.existsById(adId);
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
    ads.sort(Comparator
        .comparing(Ad::isPremium, Comparator.reverseOrder())
        .thenComparing((Ad ad) -> ad.getSeller().getRating().getAverageScore(), Comparator.reverseOrder()));
  }
}
