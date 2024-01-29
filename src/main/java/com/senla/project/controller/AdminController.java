package com.senla.project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin", description = "Предоставляет API для использования администратором")
@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

}
