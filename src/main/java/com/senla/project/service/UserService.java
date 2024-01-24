package com.senla.project.service;

import com.senla.project.dto.response.UserResponse;
import java.util.List;

public interface UserService {

  List<UserResponse> getAllUsers();

  UserResponse getUserById(Long userId);

  Long getUserIdByUsername(String username);

  boolean doesUserExist(Long userId);
}
