package com.senla.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdClosedResponse {
  private Long id;
  private String title;
  private String content;
  private int price;
  private Long buyerId;
  private int score;
}
