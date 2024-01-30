package com.senla.project.service.impl;

import com.senla.project.dto.request.UserProfileRequest;
import com.senla.project.dto.response.UserProfileResponse;
import com.senla.project.dto.response.UserResponse;
import com.senla.project.entity.User;
import com.senla.project.mapper.UserMapper;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserDetailsServiceImpl userDetailsService;

  private final UserRepository userRepository;

  private final UserMapper userMapper;


  @Override
  public UserResponse getUserById(Long userId) {
    User user = userRepository.findById(userId).get();
    return userMapper.mapToUserResponse(user);
  }

  @Override
  public Long getUserIdByUsername(String username) {
    User user = userRepository.findByUsername(username).get();
    return user.getId();
  }

  @Override
  public UserProfileResponse getUserProfileById(Long userId) {
    User user = userRepository.findById(userId).get();
    return userMapper.mapToUserProfileResponse(user);
  }

  @Override
  public boolean updateUserProfileById(UserProfileRequest userProfileRequest,
      Long userId) {

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
  public boolean doesUserExist(Long userId) {
    return userRepository.existsById(userId);
  }

  @Override
  public boolean doesUserExistByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

}
