package com.senla.project.controller;

import com.senla.project.exception.CustomValidationException;
import com.senla.project.service.SearchFilterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SearchFilter", description = "Предоставляет API для поиска и фильтрации объявлений")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/ads/search")
@AllArgsConstructor
public class SearchFilterController {

  private final SearchFilterService searchFilterService;

  @Operation(summary = "Получить отфильтрованные объявления", description = "Получает список объявлений, соответствующих заданному пользователем запросу. Тип возвращаемых объявлений зависит от параметров запроса.")
  @GetMapping
  public ResponseEntity<?> getFilteredAds(@RequestParam(required = false) String searchString,
                                          @RequestParam(required = true) String category,
                                          @RequestParam(required = false) Integer minPrice,
                                          @RequestParam(required = false) Integer maxPrice,
                                          @RequestParam(required = false) Boolean isInMyCity) {

    if (category != "open" && category != "current" && category != "closed" && category != "purchased") {
      throw new CustomValidationException("query parameter 'category' should be either open, current, closed or purchased.");
    }

    if (isInMyCity != null && category != "open") {
      throw new CustomValidationException("if query parameter 'isInMyCity' is used, query parameter 'category' should mandatory be open.");
    }

    if (minPrice != null && maxPrice != null && maxPrice < minPrice) {
      throw new CustomValidationException("query parameter 'minPrice' can't be higher than query parameter 'maxPrice'.");
    }

    if (searchString != null && searchString.length() <= 1) {
      throw new CustomValidationException("query parameter 'searchString' should either be not specified or have bigger length than 1.");
    }


    return searchFilterService.getFilteredAds();
  }
}
