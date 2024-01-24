package com.senla.project.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
  private Long id;
  private Long senderId;
  private String content;
  private LocalDateTime postedAt;
}
