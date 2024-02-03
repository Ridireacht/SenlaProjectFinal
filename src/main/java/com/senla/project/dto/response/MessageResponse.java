package com.senla.project.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {
  private Long senderId;
  private String content;
  private String postedAt;
}
