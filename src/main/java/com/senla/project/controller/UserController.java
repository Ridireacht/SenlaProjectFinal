package com.senla.project.controller;

import com.senla.project.dto.request.UserProfileRequest;
import com.senla.project.dto.response.UserProfileResponse;
import com.senla.project.dto.response.UserResponse;
import com.senla.project.exception.NotFoundException;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "Предоставляет API для управления пользователями")
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;


  @Operation(summary = "Получить пользователя по id", description = "Возвращает информацию о пользователе по его id.")
  @GetMapping("/{id}")
  public UserResponse getUser(@PathVariable("id") Long userId) {
    if (!userService.doesUserExist(userId)) {
      throw new NotFoundException("User", userId);
    }

    return userService.getUserById(userId);
  }

  @Operation(summary = "Получить профиль текущего пользователя", description = "Возвращает информацию о текущем пользователе.")
  @GetMapping("/current")
  public UserProfileResponse getCurrentUserProfile() {
    return userService.getUserProfileById(getCurrentUserId());
  }

  @Operation(summary = "Обновить профиль текущего пользователя", description = "Обновляет информацию о текущем пользователе.")
  @PutMapping("/current")
  public boolean updateCurrentUserProfile(@Valid @RequestBody UserProfileRequest userProfileRequest) {
    return userService.updateUserProfileById(userProfileRequest, getCurrentUserId());
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
