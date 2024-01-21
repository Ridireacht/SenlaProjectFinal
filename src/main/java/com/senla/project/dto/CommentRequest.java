package com.senla.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
  @NotBlank(message = "Content must not be blank")
  private String content;
}
