package com.senla.project.service.impl;

import com.senla.project.dto.response.UserProfileResponse;
import com.senla.project.dto.response.UserResponse;
import com.senla.project.entity.Ad;
import com.senla.project.entity.User;
import com.senla.project.mapper.AdMapper;
import com.senla.project.mapper.UserMapper;
import com.senla.project.repository.AdRepository;
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

  private final UserMapper userMapper;
  private final AdMapper adMapper;


  @Override
  public List<UserProfileResponse> getAllUserProfiles() {
    List<User> users = userRepository.findAll();

    return users.stream()
        .map(userMapper::mapToUserProfileResponse)
        .collect(Collectors.toList());
  }

  @Override
  public UserProfileResponse getUserProfileByUserId(Long userId) {
    User user = userRepository.findById(userId).get();
    return userMapper.mapToUserProfileResponse(user);
  }

  @Override
  public ResponseEntity<?> getFullAdInfoByAdId(Long adId) {
    Ad ad = adRepository.findById(adId).get();

    if (ad.isClosed()) {
      return ResponseEntity.ok(adMapper.mapToAdClosedResponse(ad));
    } else {
      return ResponseEntity.ok(adMapper.mapToAdCurrentResponse(ad));
    }
  }

  @Override
  @Transactional
  public boolean deleteUserById(Long userId) {
    if (userRepository.existsById(userId)) {
      userRepository.deleteById(userId);
      return true;
    }

    return false;
  }
}