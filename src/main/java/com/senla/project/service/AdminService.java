package com.senla.project.service;

import com.senla.project.dto.response.UserProfileResponse;
import java.util.List;

public interface AdminService {

  List<UserProfileResponse> getAllUserProfiles();

  UserProfileResponse getUserProfileByUserId(Long userId);

  boolean deleteUserById(Long userId);
}
