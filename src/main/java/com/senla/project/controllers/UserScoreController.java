package com.senla.project.controllers;

import com.senla.project.dto.AdPurchasedResponse;
import com.senla.project.dto.UserScoreRequest;
import com.senla.project.services.UserScoreService;
import com.senla.project.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ads/purchased/{id}")
@AllArgsConstructor
public class UserScoreController {

  private final UserScoreService userScoreService;
  private final UserService userService;


  @PostMapping
  public AdPurchasedResponse setUserScore(@PathVariable("{id}") Long adId, UserScoreRequest userScoreRequest) {
    return userScoreService.setUserScoreByAdId(getCurrentUserId(), adId, userScoreRequest);
  }

  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
