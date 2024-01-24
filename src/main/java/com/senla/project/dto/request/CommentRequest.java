package com.senla.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
  @NotBlank(message = "Content must not be blank")
  private String content;
}
