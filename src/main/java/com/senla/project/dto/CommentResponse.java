package com.senla.project.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
  private Long id;
  private String username;
  private String content;
  private LocalDateTime createdAt;
}
