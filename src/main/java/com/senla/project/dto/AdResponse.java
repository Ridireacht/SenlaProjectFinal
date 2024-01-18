package com.senla.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdResponse {
  private Long id;
  private Long sellerId;
  private String title;
  private String content;
  private int price;
}
