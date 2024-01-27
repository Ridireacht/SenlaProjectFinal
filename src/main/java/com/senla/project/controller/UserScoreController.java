package com.senla.project.controller;

import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.request.UserScoreRequest;
import com.senla.project.service.AdService;
import com.senla.project.service.UserScoreService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserScore", description = "Предоставляет API для управления оценками объявлений")
@RestController
@RequestMapping("/ads/purchased/{id}")
@AllArgsConstructor
public class UserScoreController {

  private final UserScoreService userScoreService;
  private final UserService userService;
  private final AdService adService;


  @PostMapping
  public ResponseEntity<AdPurchasedResponse> setUserScore(@PathVariable("{id}") Long adId, @Valid @RequestBody UserScoreRequest userScoreRequest) {
    if (!adService.doesAdExist(adId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if (!adService.didUserBoughtAd(adId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    if (adService.isAdAlreadyScored(adId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(userScoreService.setUserScoreByAdId(getCurrentUserId(), adId, userScoreRequest));
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
