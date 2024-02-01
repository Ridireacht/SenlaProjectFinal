package com.senla.project.controller;

import com.senla.project.dto.response.AdClosedResponse;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.dto.response.AdPurchasedResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.exception.ConflictException;
import com.senla.project.exception.CustomValidationException;
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
import org.springframework.web.bind.annotation.RequestParam;
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

  @Operation(summary = "Получить отфильтрованные объявления", description = "Получает список объявлений, соответствующих заданному администратором запросу. Тип возвращаемых объявлений зависит от параметров запроса.")
  @GetMapping("/users/{id}/ads")
  public List<AdCurrentResponse> getFilteredAdsOfUser(@PathVariable("id") Long userId,
      @RequestParam(required = false) String searchString,
      @RequestParam(required = true) String category,
      @RequestParam(required = false) Integer minPrice,
      @RequestParam(required = false) Integer maxPrice) {

    if (!adminService.doesUserExist(userId)) {
      throw new NotFoundException("User", userId);
    }

    if (category != "current" && category != "closed" && category != "purchased") {
      throw new CustomValidationException("query parameter 'category' should be either current, closed or purchased.");
    }

    if (minPrice != null && maxPrice != null && maxPrice < minPrice) {
      throw new CustomValidationException("query parameter 'minPrice' can't be higher than query parameter 'maxPrice'.");
    }

    if (searchString != null && searchString.length() <= 1) {
      throw new CustomValidationException("query parameter 'searchString' should either be not specified or have bigger length than 1.");
    }

    return adminService.getFilteredAdsForUser(userId, searchString, category, minPrice, maxPrice);
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
