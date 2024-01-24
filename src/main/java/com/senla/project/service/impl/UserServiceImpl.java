package com.senla.project.service.impl;

import com.senla.project.dto.UserResponse;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;


  @Override
  public List<UserResponse> getAllUsers() {
    return null;
  }

  @Override
  public UserResponse getUserById(Long userId) {
    return null;
  }

  @Override
  public Long getUserIdByUsername(String username) {
    return null;
  }

  @Override
  public boolean doesUserExist(Long userId) {
    return false;
  }

}
