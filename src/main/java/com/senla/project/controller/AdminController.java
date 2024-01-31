package com.senla.project.controller;

import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.exception.ConflictException;
import com.senla.project.exception.ForbiddenException;
import com.senla.project.exception.NotFoundException;
import com.senla.project.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Admin", description = "Предоставляет API для использования администратором")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

  private final AdminService adminService;


  @Operation(summary = "Получить профили всех пользователей", description = "Возвращает список профилей всех зарегистрированных пользователей.")
  @GetMapping("/users")
  public List<UserFullProfileResponse> getUserFullProfiles() {
    return adminService.getUserFullProfiles();
  }

  @Operation(summary = "Получить профиль пользователя по id", description = "Возвращает полную информацию о пользователе по его id.")
  @GetMapping("/users/{id}")
  public UserFullProfileResponse getUserFullProfile(@PathVariable("id") Long userId) {
    if (!adminService.doesUserExist(userId)) {
      throw new NotFoundException("User", userId);
    }

    return adminService.getUserFullProfile(userId);
  }

  @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя. Возвращает true, если операция удалась; false, если к моменту исполнения кода сущность перестала существовать; и 500 Internal Server Error, если возникло исключение.")
  @DeleteMapping("/users/{id}")
  public Boolean deleteUser(@PathVariable("id") Long userId) {
    if (!adminService.doesUserExist(userId)) {
      throw new NotFoundException("User", userId);
    }

    return adminService.deleteUser(userId);
  }

  @Operation(summary = "Удалить комментарий", description = "Удаляет существующий комментарий. Возвращает true, если операция удалась; false, если к моменту исполнения кода сущность перестала существовать; и 500 Internal Server Error, если возникло исключение.")
  @DeleteMapping("/comments/{id}")
  public Boolean deleteComment(@PathVariable("id") Long commentId) {
    if (!adminService.doesCommentExist(commentId)) {
      throw new NotFoundException("Comment", commentId);
    }

    if (adminService.isAdClosed(adminService.getAdId(commentId))) {
      throw new ForbiddenException("You can't delete a comment on a closed ad.");
    }

    return adminService.deleteComment(commentId);
  }

  @Operation(summary = "Получить активные объявления пользователя", description = "Получает список всех активных объявлений выбранного пользователя")
  @GetMapping("/users/{id}/ads/current")
  public List<AdCurrentResponse> getCurrentAdsOfUser(@PathVariable("id") Long userId) {
    if (!adminService.doesUserExist(userId)) {
      throw new NotFoundException("User", userId);
    }

    return adminService.getCurrentAdsOfUser(userId);
  }

  @Operation(summary = "Получить закрытые объявления пользователя", description = "Получает список всех неактивных (закрытых) объявлений выбранного пользователя.")
  @GetMapping("/users/{id}/ads/closed")
  public List<AdClosedResponse> getClosedAdsOfUser(@PathVariable("id") Long userId) {
    if (!adminService.doesUserExist(userId)) {
      throw new NotFoundException("User", userId);
    }

    return adminService.getClosedAdsOfUser(userId);
  }

  @Operation(summary = "Получить выкупленные объявления пользователя", description = "Получает список всех выкупленных выбранным пользователем объявлений.")
  @GetMapping("/users/{id}/ads/purchased")
  public List<AdPurchasedResponse> getPurchasedAdsOfUser(@PathVariable("id") Long userId) {
    if (!adminService.doesUserExist(userId)) {
      throw new NotFoundException("User", userId);
    }

    return adminService.getPurchasedAdsOfUser(userId);
  }

  @Operation(summary = "Получить информацию по конкретному объявлению", description = "Получает полную информацию о конкретном объявлении по его id.")
  @GetMapping("/ads/{id}")
  public ResponseEntity<?> getAd(@PathVariable("id") Long adId) {
    if (!adminService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    return adminService.getAd(adId);
  }

  @Operation(summary = "Удалить объявление", description = "Полностью удаляет объявление по его id. Возвращает true, если операция удалась; false, если к моменту исполнения кода сущность перестала существовать; и 500 Internal Server Error, если возникло исключение.")
  @DeleteMapping("/ads/{id}")
  public Boolean deleteAd(@PathVariable("id") Long adId) {
    if (!adminService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    if (adminService.isAdClosed(adId)) {
      throw new ForbiddenException("You can't delete a closed ad.");
    }

    return adminService.deleteAd(adId);
  }

  @Operation(summary = "Убрать премиальный статус у объявления", description = "Убирает премиальный статус у объявления по его id. Возвращает true, если операция удалась; false, если к моменту исполнения кода сущность перестала существовать; и 500 Internal Server Error, если возникло исключение.")
  @PutMapping("/ads/{id}/premium")
  public Boolean unmakeAdPremium(@PathVariable("id") Long adId) {
    if (!adminService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    if (adminService.isAdClosed(adId)) {
      throw new ForbiddenException("You can't remove premium status from a closed ad.");
    }

    if (!adminService.isAdPremium(adId)) {
      throw new ConflictException("You can't remove a premium status if ad doesn't have one.");
    }

    return adminService.unmakeAdPremium(adId);
  }
}
