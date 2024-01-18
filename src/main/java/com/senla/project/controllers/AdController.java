package com.senla.project.controllers;

import com.senla.project.dto.AdClosedResponse;
import com.senla.project.dto.AdCurrentResponse;
import com.senla.project.dto.AdPurchasedResponse;
import com.senla.project.dto.AdRequest;
import com.senla.project.dto.AdResponse;
import com.senla.project.services.AdService;
import com.senla.project.services.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ads")
@AllArgsConstructor
public class AdController {

  private final AdService adService;
  private final UserService userService;


  @GetMapping
  public List<AdResponse> getAllAds() {
    return adService.getAllAds();
  }

  @GetMapping("/current")
  public List<AdCurrentResponse> getCurrentAds() {
    return adService.getCurrentAdsByUserId(getCurrentUserId());
  }

  @GetMapping("/closed")
  public List<AdClosedResponse> getClosedAds() {
    return adService.getClosedAdsByUserId(getCurrentUserId());
  }

  @GetMapping("/purchased")
  public List<AdPurchasedResponse> getPurchasedAds() {
    return adService.getPurchasedAdsByUserId(getCurrentUserId());
  }

  @GetMapping("/{id}")
  public AdResponse getAd(@PathVariable("id") Long id) {
    return adService.getAdById(id);
  }

  @PostMapping
  public AdResponse createAd(AdRequest adRequest) {
    return adService.createAd(adRequest);
  }

  @PutMapping("/{id}")
  public AdResponse updateAd(@PathVariable("id") Long id, AdRequest adRequest) {
    return adService.updateAd(adRequest);
  }

  @PutMapping("/{id}/premium")
  public AdResponse makeAdPremium(@PathVariable("id") Long id) {
    return adService.makeAdPremium(id);
  }

  @DeleteMapping("/{id}")
  public boolean deleteAd(@PathVariable("id") Long id) {
    return adService.deleteAd(id);
  }


  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
