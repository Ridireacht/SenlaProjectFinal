package com.senla.project.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdFullOpenResponse {
  private Long id;
  private Long sellerId;
  private String sellerCity;
  private String postedAt;
  private String title;
  private String content;
  private int price;
  private boolean isPremium;
  private double sellerRating;
}
