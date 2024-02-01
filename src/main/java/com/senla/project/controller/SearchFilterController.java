package com.senla.project.controller;

import com.senla.project.service.SearchFilterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SearchFilter", description = "Предоставляет API для поиска и фильтрации объявлений")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@AllArgsConstructor
public class SearchFilterController {

  private final SearchFilterService searchFilterService;
}
