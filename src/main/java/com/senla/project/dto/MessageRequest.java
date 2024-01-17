package com.senla.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
  private Long adId;
  private String content;
}
