package com.senla.project.service.impl;

import com.senla.project.dto.request.UserProfileRequest;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.dto.response.UserBriefProfileResponse;
import com.senla.project.entity.User;
import com.senla.project.mapper.UserMapper;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserDetailsServiceImpl userDetailsService;

  private final UserRepository userRepository;

  private final UserMapper userMapper;


  @Override
  public UserBriefProfileResponse getUserBriefProfile(long userId) {
    User user = userRepository.findById(userId).get();
    return userMapper.mapToUserResponse(user);
  }

  @Override
  public long getUserIdByUsername(String username) {
    User user = userRepository.findByUsername(username).get();
    return user.getId();
  }

  @Override
  public UserFullProfileResponse getUserFullProfile(long userId) {
    User user = userRepository.findById(userId).get();
    return userMapper.mapToUserProfileResponse(user);
  }

  @Override
  public boolean updateUserProfile(UserProfileRequest userProfileRequest,
      long userId) {

    User user = userRepository.findById(userId).get();

    if (userProfileRequest.getEmail() != null && userProfileRequest.getEmail() != "") {
      user.setEmail(userProfileRequest.getEmail());
    }

    if (userProfileRequest.getAddress() != null && userProfileRequest.getAddress() != "") {
      user.setAddress(userProfileRequest.getAddress());
    }

    if (userProfileRequest.getPassword() != null && userProfileRequest.getPassword() != "") {
      user.setPassword(userDetailsService.encodePassword(userProfileRequest.getPassword()));
    }

    userRepository.save(user);

    return true;
  }

  @Override
  public boolean doesUserExist(long userId) {
    return userRepository.existsById(userId);
  }

  @Override
  public boolean doesUserExistByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

}
