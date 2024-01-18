package com.senla.project.controllers;

import com.senla.project.dto.AdPurchasedResponse;
import com.senla.project.dto.UserScoreRequest;
import com.senla.project.repositories.UserScoreRepository;
import com.senla.project.services.UserScoreService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ads/purchased/{id}")
@AllArgsConstructor
public class UserScoreController {

  private final UserScoreService userScoreService;


  @PostMapping
  public AdPurchasedResponse setUserScoreByAdId(@PathVariable("{id}") Long id, UserScoreRequest userScoreRequest) {
    return userScoreService.setUserScoreByAdId(id, userScoreRequest);
  }
}
