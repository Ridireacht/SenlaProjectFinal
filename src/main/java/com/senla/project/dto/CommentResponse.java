package com.senla.project.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
  private String username;
  private String content;
  private LocalDateTime createdAt;
}
