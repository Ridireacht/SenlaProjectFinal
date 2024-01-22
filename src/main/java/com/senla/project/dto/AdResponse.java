package com.senla.project.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdResponse {
  private Long id;
  private Long sellerId;
  private LocalDateTime postedAt;
  private String title;
  private String content;
  private int price;
}
