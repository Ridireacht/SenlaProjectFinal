package com.senla.project.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdRequest {
  @NotBlank(message = "Title must not be blank")
  private String title;

  @NotBlank(message = "Content must not be blank")
  private String content;

  @NotNull(message = "Price must not be null")
  @Min(value = 1, message = "Price must be greater than 0")
  private int price;
}
