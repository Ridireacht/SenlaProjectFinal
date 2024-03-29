package com.senla.project.controller;

import com.senla.project.dto.request.ScoreRequest;
import com.senla.project.exception.ForbiddenException;
import com.senla.project.exception.NotFoundException;
import com.senla.project.service.AdService;
import com.senla.project.service.AuthService;
import com.senla.project.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Score", description = "Предоставляет API для управления оценками объявлений")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/ads/purchased/{id}")
@AllArgsConstructor
public class ScoreController {

  private final ScoreService scoreService;
  private final AdService adService;
  private final AuthService authService;


  @Operation(summary = "Установить оценку объявлению", description = "Устанавливает оценку объявлению, которое было куплено текущим пользователем. Возвращает true, если операция удалась; false, если к моменту исполнения кода сущность перестала существовать; и 500 Internal Server Error, если возникло исключение.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "404", description = "Сущность не найдена", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @PostMapping
  public Boolean setScoreToAd(@PathVariable("id") Long adId, @Valid @RequestBody ScoreRequest scoreRequest) {
    if (!adService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    if (!adService.isAdSoldToUser(adId, authService.getCurrentUserId())) {
      throw new ForbiddenException("You can't set a score on ad you didn't buy.");
    }

    if (adService.isAdScored(adId)) {
      throw new ForbiddenException("You can't set a new score, ad already has one.");
    }

    return scoreService.setScoreToAd(authService.getCurrentUserId(), adId, scoreRequest);
  }
}
