package com.senla.project.service.impl;

import com.senla.project.dto.response.UserResponse;
import com.senla.project.entity.User;
import com.senla.project.mapper.AdMapper;
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

  private final UserRepository userRepository;
  private final UserMapper mapper;


  @Override
  public List<UserResponse> getAllUsers() {
    List<User> users = userRepository.findAll();

    return users.stream()
        .map(mapper::mapToUserResponse)
        .collect(Collectors.toList());
  }

  @Override
  public UserResponse getUserById(Long userId) {
    User user = userRepository.findById(userId).orElse(null);
    return mapper.mapToUserResponse(user);
  }

  @Override
  public Long getUserIdByUsername(String username) {
    User user = userRepository.findByUsername(username).orElse(null);
    return user.getId();
  }

  @Override
  public boolean doesUserExist(Long userId) {
    return userRepository.existsById(userId);
  }

}
