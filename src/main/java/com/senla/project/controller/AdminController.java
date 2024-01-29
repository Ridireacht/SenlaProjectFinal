package com.senla.project.controller;

import com.senla.project.dto.response.UserProfileResponse;
import com.senla.project.dto.response.UserResponse;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin", description = "Предоставляет API для использования администратором")
@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

  private final UserService userService;


  @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех зарегистрированных пользователей.")
  @GetMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public List<UserProfileResponse> getAllUserProfiles() {
    return userService.getAllUserProfiles();
  }
}
