package com.senla.project.controller;

import com.senla.project.dto.request.LoginRequest;
import com.senla.project.dto.request.RegisterRequest;
import com.senla.project.exception.ConflictException;
import com.senla.project.exception.CustomValidationException;
import com.senla.project.security.JwtService;
import com.senla.project.service.AuthService;
import com.senla.project.service.impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Предоставляет API для регистрации и авторизации пользователей")
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

  private final UserDetailsServiceImpl userDetailsService;
  private final JwtService jwtService;
  private final AuthService authService;

  private final AuthenticationManager authenticationManager;


  @Operation(summary = "Зарегистрировать нового пользователя", description = "Регистрирует пользователя с данными из формы.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "409", description = "Конфликт данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @PostMapping("/register")
  public String register(@Valid @RequestBody RegisterRequest registerRequest) {
    if (authService.doesUserExistByUsername(registerRequest.getUsername())) {
      throw new ConflictException("This username is already taken.");
    }

    if (authService.doesUserExistByEmail(registerRequest.getEmail())) {
      throw new ConflictException("This email is already taken.");
    }

    if (!authService.doesRoleExistByName(registerRequest.getRole())) {
      throw new CustomValidationException("this role doesn't exist.");
    }

    return userDetailsService.registerUser(registerRequest);
  }

  @Operation(summary = "Аутентифицировать и авторизовать существующего пользователя", description = "Аутентифицирует и авторизует пользователя и возвращает JWT-токен.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @PostMapping("/login")
  public String login(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    if (authentication.isAuthenticated()) {
      return jwtService.generateToken(loginRequest.getUsername());
    } else {
      throw new UsernameNotFoundException("Invalid user request: username not found!");
    }
  }
}
