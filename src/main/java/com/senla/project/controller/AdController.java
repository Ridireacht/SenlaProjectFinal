package com.senla.project.controller;

import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdResponse;
import com.senla.project.service.AdService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Объявление", description = "API для управления объявлениями")
@RestController
@RequestMapping("/ads")
@AllArgsConstructor
public class AdController {

  private final AdService adService;
  private final UserService userService;


  @Operation(summary = "Get all ads from others", description = "Получить список всех активных объявлений, которые не принадлежат текущему пользователю")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешная операция")
  })
  @GetMapping
  public List<AdResponse> getAllAdsFromOthers() {
    return adService.getAllAdsFromOthers(getCurrentUserId());
  }

  @Operation(summary = "Get current ads", description = "Получить список всех активных объявлений текущего пользователя")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешная операция")
  })
  @GetMapping("/current")
  public List<AdCurrentResponse> getCurrentAds() {
    return adService.getCurrentAdsByUserId(getCurrentUserId());
  }

  @Operation(summary = "Get closed ads", description = "Получить список всех неактивных (закрытых) объявлений текущего пользователя")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешная операция")
  })
  @GetMapping("/closed")
  public List<AdClosedResponse> getClosedAds() {
    return adService.getClosedAdsByUserId(getCurrentUserId());
  }

  @Operation(summary = "Get purchased ads", description = "Получить список всех выкупленных текущим пользователем объявлений")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешная операция")
  })
  @GetMapping("/purchased")
  public List<AdPurchasedResponse> getPurchasedAds() {
    return adService.getPurchasedAdsByUserId(getCurrentUserId());
  }

  @Operation(summary = "Get ad", description = "Получить конкретное объявление по его id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешная операция"),
      @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
      @ApiResponse(responseCode = "403", description = "Объявление не доступно текущему пользователю")
  })
  @GetMapping("/{id}")
  public ResponseEntity<AdResponse> getAd(@PathVariable("id") Long adId) {
    if (!adService.doesAdExist(adId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if (!adService.isAdAvailableForUser(adId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(adService.getAdById(adId));
  }

  @Operation(summary = "Create ad", description = "Создать новое объявление по форме-реквесту")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешная операция")
  })
  @PostMapping
  public AdResponse createAd(@Valid @RequestBody AdRequest adRequest) {
    return adService.createAd(getCurrentUserId(), adRequest);
  }

  @Operation(summary = "Update ad", description = "Обновить существующее объявление по форме-реквесту")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешная операция"),
      @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
      @ApiResponse(responseCode = "403", description = "Объявление не принадлежит текущему пользователю или уже закрыто")
  })
  @PutMapping("/{id}")
  public ResponseEntity<AdResponse> updateAd(@PathVariable("id") Long adId, @Valid @RequestBody AdRequest adRequest) {
    if (!adService.doesAdExist(adId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if (!adService.doesAdBelongToUser(adId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    if (adService.isAdClosed(adId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(adService.updateAd(adId, adRequest));
  }

  @Operation(summary = "Make ad premium", description = "Сделать объявление премиальным по его id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешная операция"),
      @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
      @ApiResponse(responseCode = "403", description = "Объявление не принадлежит текущему пользователю или уже закрыто")
  })
  @PutMapping("/{id}/premium")
  public ResponseEntity<AdResponse> makeAdPremium(@PathVariable("id") Long adId) {
    if (!adService.doesAdExist(adId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if (!adService.doesAdBelongToUser(adId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    if (adService.isAdClosed(adId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(adService.makeAdPremium(adId));
  }

  @Operation(summary = "Delete ad", description = "Полностью удалить объявление по его id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешная операция"),
      @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
      @ApiResponse(responseCode = "403", description = "Объявление не принадлежит текущему пользователю или уже закрыто")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> deleteAd(@PathVariable("id") Long adId) {
    if (!adService.doesAdExist(adId)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    if (!adService.doesAdBelongToUser(adId, getCurrentUserId())) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    if (adService.isAdClosed(adId)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    return ResponseEntity.ok(adService.deleteAd(adId));
  }


  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
