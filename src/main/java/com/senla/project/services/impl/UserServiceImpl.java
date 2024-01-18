package com.senla.project.services.impl;

import com.senla.project.dto.UserResponse;
import com.senla.project.repositories.UserRepository;
import com.senla.project.services.UserService;
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
  public UserResponse getUserById(Long id) {
    return null;
  }
}
