package com.senla.project.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdClosedResponse {
  private Long id;
  private LocalDateTime postedAt;
  private String title;
  private String content;
  private int price;
  private Long buyerId;
  private int score;
}
