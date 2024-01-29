package com.senla.project.service.impl;

import com.senla.project.repository.RoleRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;


  @Override
  public boolean doesUserExistByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  @Override
  public boolean doesUserExistByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public boolean doesRoleExistByName(String name) {
    return roleRepository.existsByName(name);
  }
}
