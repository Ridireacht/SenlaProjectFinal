package com.senla.project.controller;

import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.request.UserScoreRequest;
import com.senla.project.exception.ForbiddenException;
import com.senla.project.exception.NotFoundException;
import com.senla.project.service.AdService;
import com.senla.project.service.UserScoreService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserScore", description = "Предоставляет API для управления оценками объявлений")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/ads/purchased/{id}")
@AllArgsConstructor
public class UserScoreController {

  private final UserScoreService userScoreService;
  private final UserService userService;
  private final AdService adService;


  @Operation(summary = "Установить оценку объявлению", description = "Устанавливает оценку объявлению, которое было куплено текущим пользователем. Возвращает информацию об этом объявлении.")
  @PostMapping
  public AdPurchasedResponse setUserScore(@PathVariable("{id}") Long adId, @Valid @RequestBody UserScoreRequest userScoreRequest) {
    if (!adService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    if (!adService.didUserBoughtAd(adId, getCurrentUserId())) {
      throw new ForbiddenException("You can't set a score on ad you didn't buy.");
    }

    if (adService.isAdAlreadyScored(adId)) {
      throw new ForbiddenException("You can't set a new score, ad already has one.");
    }

    return userScoreService.setUserScoreByAdId(getCurrentUserId(), adId, userScoreRequest);
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
