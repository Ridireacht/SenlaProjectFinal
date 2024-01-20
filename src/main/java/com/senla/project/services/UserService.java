package com.senla.project.services;

import com.senla.project.dto.UserResponse;
import java.util.List;

public interface UserService {

  List<UserResponse> getAllUsers();

  UserResponse getUserById(Long userId);

  Long getUserIdByUsername(String username);
}
