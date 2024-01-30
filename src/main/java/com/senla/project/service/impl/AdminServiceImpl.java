package com.senla.project.service.impl;

import com.senla.project.dto.response.UserProfileResponse;
import com.senla.project.entity.User;
import com.senla.project.mapper.UserMapper;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.AdminService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;

  private final UserMapper userMapper;


  @Override
  public List<UserProfileResponse> getAllUserProfiles() {
    List<User> users = userRepository.findAll();

    return users.stream()
        .map(userMapper::mapToUserProfileResponse)
        .collect(Collectors.toList());
  }
}
