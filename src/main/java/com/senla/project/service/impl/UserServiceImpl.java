package com.senla.project.service.impl;

import com.senla.project.dto.request.UserProfileRequest;
import com.senla.project.dto.response.UserBriefProfileResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.User;
import com.senla.project.mapper.UserMapper;
import com.senla.project.repository.AdRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserDetailsServiceImpl userDetailsService;

  private final UserRepository userRepository;
  private final AdRepository adRepository;

  private final UserMapper userMapper;


  @Override
  public UserBriefProfileResponse getUserBriefProfile(long userId) {
    User user = userRepository.findById(userId).get();
    return userMapper.mapToUserBriefProfileResponse(user);
  }

  @Override
  public long getUserIdByUsername(String username) {
    User user = userRepository.findByUsername(username).get();
    return user.getId();
  }

  @Override
  public UserFullProfileResponse getUserFullProfile(long userId) {
    User user = userRepository.findById(userId).get();
    return userMapper.mapToUserFullProfileResponse(user);
  }

  @Override
  public boolean updateUserProfile(UserProfileRequest userProfileRequest,
      long userId) {

    if (userRepository.existsById(userId)) {
      User user = userRepository.findById(userId).get();

      if (userProfileRequest.getEmail() != null) {
        user.setEmail(userProfileRequest.getEmail());
      }

      if (userProfileRequest.getAddress() != null) {
        user.setAddress(userProfileRequest.getAddress());
      }

      if (userProfileRequest.getPassword() != null) {
        user.setPassword(userDetailsService.encodePassword(userProfileRequest.getPassword()));
      }

      userRepository.save(user);
      return true;
    }

    return false;
  }

  @Override
  public boolean doesUserExist(long userId) {
    return userRepository.existsById(userId);
  }

  @Override
  public boolean doesUserExistByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public boolean isUserBuyerOrSellerOfAd(long userId, long adId) {
    Ad ad = adRepository.findById(adId).get();
    return ad.getSeller().getId() == userId || ad.getBuyer().getId() == userId;
  }

}
