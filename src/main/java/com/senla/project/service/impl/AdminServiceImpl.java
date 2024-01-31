package com.senla.project.service.impl;

import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.Comment;
import com.senla.project.entity.User;
import com.senla.project.mapper.AdMapper;
import com.senla.project.mapper.UserMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.CommentRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.AdminService;
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

  private final UserMapper userMapper;
  private final AdMapper adMapper;


  @Override
  public List<AdCurrentResponse> getCurrentAdsOfUser(Long userId) {
    List<Ad> currentAds = adRepository.findAllBySellerIdAndIsClosedFalse(userId);
    return currentAds.stream()
        .map(adMapper::mapToAdCurrentResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<AdClosedResponse> getClosedAdsOfUser(Long userId) {
    List<Ad> closedAds = adRepository.findAllBySellerIdAndIsClosedTrue(userId);
    return closedAds.stream()
        .map(adMapper::mapToAdClosedResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<AdPurchasedResponse> getPurchasedAdsOfUser(Long userId) {
    List<Ad> purchasedAds = adRepository.findAllByBuyerId(userId);
    return purchasedAds.stream()
        .map(adMapper::mapToAdPurchasedResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<UserFullProfileResponse> getUserFullProfiles() {
    List<User> users = userRepository.findAll();

    return users.stream()
        .map(userMapper::mapToUserProfileResponse)
        .collect(Collectors.toList());
  }

  @Override
  public UserFullProfileResponse getUserFullProfile(Long userId) {
    User user = userRepository.findById(userId).get();
    return userMapper.mapToUserProfileResponse(user);
  }

  @Override
  public ResponseEntity<?> getAd(Long adId) {
    Ad ad = adRepository.findById(adId).get();

    if (ad.isClosed()) {
      return ResponseEntity.ok(adMapper.mapToAdClosedResponse(ad));
    } else {
      return ResponseEntity.ok(adMapper.mapToAdCurrentResponse(ad));
    }
  }

  @Override
  public Long getAdId(Long commentId) {
    Comment comment = commentRepository.findById(commentId).get();
    return comment.getAd().getId();
  }

  @Transactional
  @Override
  public boolean unmakeAdPremium(Long adId) {
    if (adRepository.existsById(adId)) {
      Ad ad = adRepository.findById(adId).get();
      ad.setPremium(false);
      adRepository.save(ad);

      return true;
    }

    return false;
  }

  @Transactional
  @Override
  public boolean deleteUser(Long userId) {
    if (userRepository.existsById(userId)) {
      userRepository.deleteById(userId);
      return true;
    }

    return false;
  }

  @Transactional
  @Override
  public boolean deleteComment(Long commentId) {
    if (commentRepository.existsById(commentId)) {
      commentRepository.deleteById(commentId);
      return true;
    }

    return false;
  }

  @Transactional
  @Override
  public boolean deleteAd(Long adId) {
    if (adRepository.existsById(adId)) {
      adRepository.deleteById(adId);
      return true;
    }

    return false;
  }

  @Override
  public boolean doesUserExist(Long userId) {
    return userRepository.existsById(userId);
  }

  @Override
  public boolean doesCommentExist(Long commentId) {
    return commentRepository.existsById(commentId);
  }

  @Override
  public boolean doesAdExist(Long adId) {
    return adRepository.existsById(adId);
  }

  @Override
  public boolean isAdClosed(Long adId) {
    Ad ad = adRepository.findById(adId).get();
    return ad.isClosed();
  }

  @Override
  public boolean isAdPremium(Long adId) {
    Ad ad = adRepository.findById(adId).get();
    return ad.isPremium();
  }
}
