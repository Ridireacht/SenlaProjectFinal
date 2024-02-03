package com.senla.project.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdPurchasedResponse {
  private Long id;
  private String postedAt;
  private String title;
  private String content;
  private int price;
  private int score;
}
