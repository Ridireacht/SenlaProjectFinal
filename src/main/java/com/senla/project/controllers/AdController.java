package com.senla.project.controllers;

import com.senla.project.dto.AdRequest;
import com.senla.project.dto.AdResponse;
import com.senla.project.services.AdService;
import java.util.List;
import lombok.AllArgsConstructor;
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


  @GetMapping
  public List<AdResponse> getAllAds() {
    return adService.getAllAds();
  }

  @GetMapping("/current")
  public List<AdResponse> getCurrentAds() {
    return adService.getCurrentAdsByUserId();
  }

  @GetMapping("/closed")
  public List<AdResponse> getClosedAds() {
    return adService.getClosedAdsByUserId();
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
}
