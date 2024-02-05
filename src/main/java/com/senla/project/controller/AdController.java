package com.senla.project.controller;

import com.senla.project.dto.request.AdRequest;
import com.senla.project.dto.response.AdCurrentResponse;
import com.senla.project.exception.ConflictException;
import com.senla.project.exception.CustomValidationException;
import com.senla.project.exception.ForbiddenException;
import com.senla.project.exception.NotFoundException;
import com.senla.project.service.AdService;
import com.senla.project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ad", description = "Предоставляет API для управления объявлениями")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/ads")
@AllArgsConstructor
public class AdController {

  private final AdService adService;
  private final UserService userService;


  @Operation(summary = "Получить отфильтрованные объявления", description = "Получает список объявлений, соответствующих заданному пользователем запросу. Тип возвращаемых объявлений зависит от параметров запроса.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @GetMapping
  public ResponseEntity<?> getFilteredAds(@RequestParam(required = false) String searchString,
      @RequestParam(required = true) String category,
      @RequestParam(required = false) Integer minPrice,
      @RequestParam(required = false) Integer maxPrice,
      @RequestParam(required = false) Boolean isInMyCity) {

    if (!category.equals("open") && !category.equals("current") && !category.equals("closed") && !category.equals("purchased")) {
      throw new CustomValidationException("query parameter 'category' should be either open, current, closed or purchased.");
    }

    if (isInMyCity != null && !category.equals("open")) {
      throw new CustomValidationException("if query parameter 'isInMyCity' is used, query parameter 'category' should mandatory be open.");
    }

    if (minPrice != null && maxPrice != null && maxPrice < minPrice) {
      throw new CustomValidationException("query parameter 'minPrice' can't be higher than query parameter 'maxPrice'.");
    }

    if (searchString != null && searchString.length() <= 1) {
      throw new CustomValidationException("query parameter 'searchString' should either be not specified or have bigger length than 1.");
    }

    return adService.getFilteredAdsForUser(getCurrentUserId(), searchString, category, minPrice, maxPrice, isInMyCity);
  }

  @Operation(summary = "Получить конкретное объявление", description = "Получает конкретное объявление по его id. Тип возвращаемого объявления зависит от текущего пользователя и статуса объявления.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "404", description = "Сущность не найдена", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @GetMapping("/{id}")
  public ResponseEntity<?> getAd(@PathVariable("id") Long adId) {
    if (!adService.doesAdExist(adId)) {
      throw new NotFoundException("Ad", adId);
    }

    if (adService.isAdClosed(adId) && !userService.isUserBuyerOrSellerOfAd(getCurrentUserId(), adId)) {
      throw new ForbiddenException("This ad is closed and not available for you.");
    }

    return adService.getAd(adId, getCurrentUserId());
  }

  @Operation(summary = "Создать объявление", description = "Создаёт новое объявление по форме-реквесту. Возвращает информацию об этом объявлении.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
  @PostMapping
  public AdCurrentResponse createAd(@Valid @RequestBody AdRequest adRequest) {
    return adService.createAd(getCurrentUserId(), adRequest);
  }

  @Operation(summary = "Обновить объявление", description = "Обновляет существующее объявление по форме-реквесту. Возвращает true, если операция удалась; false, если к моменту исполнения кода сущность перестала существовать; и 500 Internal Server Error, если возникло исключение.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "404", description = "Сущность не найдена", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
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

  @Operation(summary = "Сделать объявление премиальным", description = "Делает объявление премиальным по его id. Возвращает true, если операция удалась; false, если к моменту исполнения кода сущность перестала существовать; и 500 Internal Server Error, если возникло исключение.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "404", description = "Сущность не найдена", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "409", description = "Конфликт данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
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

  @Operation(summary = "Удалить объявление", description = "Полностью удаляет объявление по его id. Возвращает true, если операция удалась; false, если к моменту исполнения кода сущность перестала существовать; и 500 Internal Server Error, если возникло исключение.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Успешное выполнение операции"),
      @ApiResponse(responseCode = "400", description = "Некорректный запрос или ошибка валидации данных", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "403", description = "Доступ запрещён", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "404", description = "Сущность не найдена", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
      @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
  })
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
