package com.senla.project.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdCurrentResponse {
  private Long id;
  private String postedAt;
  private String title;
  private String content;
  private int price;
  private boolean isPremium;
}
