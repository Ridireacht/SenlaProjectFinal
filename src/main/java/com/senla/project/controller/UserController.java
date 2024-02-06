package com.senla.project.controller;

import com.senla.project.dto.request.UserProfileRequest;
import com.senla.project.dto.response.UserBriefProfileResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.exception.ConflictException;
import com.senla.project.exception.CustomValidationException;
import com.senla.project.exception.NotFoundException;
import com.senla.project.service.AuthService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "Предоставляет API для управления пользователями")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

  private final UserService userService;
  private final AuthService authService;


  @Operation(summary = "Получить пользователя по id", description = "Возвращает информацию о пользователе по его id.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "404", description = "Сущность не найдена", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @GetMapping("/{id}")
  public UserBriefProfileResponse getUserBriefProfile(@PathVariable("id") Long userId) {
    if (!userService.doesUserExist(userId)) {
      throw new NotFoundException("User", userId);
    }

    return userService.getUserBriefProfile(userId);
  }

  @Operation(summary = "Получить профиль текущего пользователя", description = "Возвращает информацию о текущем пользователе.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @GetMapping("/current")
  public UserFullProfileResponse getCurrentUserFullProfile() {
    return userService.getUserFullProfile(authService.getCurrentUserId());
  }

  @Operation(summary = "Обновить профиль текущего пользователя", description = "Обновляет информацию о текущем пользователе. Обновляются только не-пустые указанные поля, соответствующие валидации. Возвращает true, если операция удалась; false, если к моменту исполнения кода сущность перестала существовать; и 500 Internal Server Error, если возникло исключение.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "409", description = "Конфликт данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @PutMapping("/current")
  public Boolean updateCurrentUserProfile(@Valid @RequestBody UserProfileRequest userProfileRequest) {
    if (userService.doesUserExistByEmail(userProfileRequest.getEmail())) {
      throw new ConflictException("This email is already taken.");
    }

    if (userProfileRequest.getEmail() == null && userProfileRequest.getAddress() == null && userProfileRequest.getPassword() == null) {
      throw new CustomValidationException("the request is empty.");
    }

    return userService.updateUserProfile(userProfileRequest, authService.getCurrentUserId());
  }
}
