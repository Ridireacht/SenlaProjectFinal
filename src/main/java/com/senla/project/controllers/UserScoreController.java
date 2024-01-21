package com.senla.project.controllers;

import com.senla.project.dto.AdPurchasedResponse;
import com.senla.project.dto.UserScoreRequest;
import com.senla.project.services.AdService;
import com.senla.project.services.UserScoreService;
import com.senla.project.services.UserService;
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
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    return ResponseEntity.ok(userScoreService.setUserScoreByAdId(getCurrentUserId(), adId, userScoreRequest));
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
