package com.senla.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdPurchasedResponse {
  private Long id;
  private String title;
  private String content;
  private int price;
  private int score;
}
