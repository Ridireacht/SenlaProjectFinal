package com.senla.project.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBriefProfileResponse {
  private Long id;
  private String username;
  private String address;
  private Double rating;
}
