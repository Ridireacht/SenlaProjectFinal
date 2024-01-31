package com.senla.project.controller;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.response.AdOpenResponse;
import com.senla.project.exception.ConflictException;
import com.senla.project.exception.ForbiddenException;
import com.senla.project.exception.NotFoundException;
import com.senla.project.service.AdService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
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

@Tag(name = "Ad", description = "Предоставляет API для управления объявлениями")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/ads")
@AllArgsConstructor
public class AdController {

  private final AdService adService;
  private final UserService userService;


  @Operation(summary = "Получить объявления от других пользователей", description = "Получает список всех активных объявлений, которые не принадлежат текущему пользователю")
  @GetMapping
  public List<AdOpenResponse> getOpenAdsFromOtherUsers() {
    return adService.getOpenAdsFromOtherUsers(getCurrentUserId());
  }

  @Operation(summary = "Получить свои активные объявления", description = "Получает список всех активных объявлений текущего пользователя")
  @GetMapping("/current")
  public List<AdCurrentResponse> getCurrentAdsOfCurrentUser() {
    return adService.getCurrentAdsOfUser(getCurrentUserId());
  }

  @Operation(summary = "Получить свои закрытые объявления", description = "Получает список всех неактивных (закрытых) объявлений текущего пользователя.")
  @GetMapping("/closed")
  public List<AdClosedResponse> getClosedAdsOfCurrentUser() {
    return adService.getClosedAdsOfUser(getCurrentUserId());
  }

  @Operation(summary = "Получить свои выкупленные объявления", description = "Получает список всех выкупленных текущим пользователем объявлений.")
  @GetMapping("/purchased")
  public List<AdPurchasedResponse> getPurchasedAdsOfCurrentUser() {
    return adService.getPurchasedAdsOfUser(getCurrentUserId());
  }

  @Operation(summary = "Получить конкретное объявление", description = "Получает конкретное объявление по его id. Тип возвращаемого объявления зависит от текущего пользователя и статуса объявления.")
  @GetMapping("/{id}")
  public ResponseEntity<?> getAd(@PathVariable("id") Long adId) {
    if (!adService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    if (!adService.isAdAvailableToUser(adId, getCurrentUserId())) {
      throw new ForbiddenException("This ad is not available for you.");
    }

    return adService.getAd(adId);
  }

  @Operation(summary = "Создать объявление", description = "Создаёт новое объявление по форме-реквесту. Возвращает информацию об этом объявлении.")
  @PostMapping
  public AdCurrentResponse createAd(@Valid @RequestBody AdRequest adRequest) {
    return adService.createAd(getCurrentUserId(), adRequest);
  }

  @Operation(summary = "Обновить объявление", description = "Обновляет существующее объявление по форме-реквесту. Возвращает true, если операция удалась, или 500 Internal Server Error, если возникло исключение.")
  @PutMapping("/{id}")
  public Boolean updateAd(@PathVariable("id") Long adId, @Valid @RequestBody AdRequest adRequest) {
    if (!adService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    if (!adService.doesAdBelongToUser(adId, getCurrentUserId())) {
      throw new ForbiddenException("You can't update someone else's ad.");
    }

    if (adService.isAdClosed(adId)) {
      throw new ForbiddenException("You can't update a closed ad.");
    }

    return adService.updateAd(adId, adRequest);
  }

  @Operation(summary = "Сделать объявление премиальным", description = "Делает объявление премиальным по его id. Возвращает true, если операция удалась, или 500 Internal Server Error, если возникло исключение.")
  @PutMapping("/{id}/premium")
  public Boolean makeAdPremium(@PathVariable("id") Long adId) {
    if (!adService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    if (!adService.doesAdBelongToUser(adId, getCurrentUserId())) {
      throw new ForbiddenException("You can't make someone else's ad a premium one.");
    }

    if (adService.isAdClosed(adId)) {
      throw new ForbiddenException("You can't make closed ad a premium.");
    }

    if (adService.isAdPremium(adId)) {
      throw new ConflictException("You can't make ad a premium one if it already has a premium status.");
    }

    return adService.makeAdPremium(adId);
  }

  @Operation(summary = "Удалить объявление", description = "Полностью удаляет объявление по его id. Возвращает true, если операция удалась, или 500 Internal Server Error, если возникло исключение.")
  @DeleteMapping("/{id}")
  public Boolean deleteAd(@PathVariable("id") Long adId) {
    if (!adService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    if (!adService.doesAdBelongToUser(adId, getCurrentUserId())) {
      throw new ForbiddenException("You can't delete someone else's ad.");
    }

    if (adService.isAdClosed(adId)) {
      throw new ForbiddenException("You can't delete a closed ad.");
    }

    return adService.deleteAd(adId);
  }


  private Long getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return userService.getUserIdByUsername(authentication.getName());
  }
}
