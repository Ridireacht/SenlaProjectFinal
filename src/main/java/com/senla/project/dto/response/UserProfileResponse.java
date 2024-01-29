package com.senla.project.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponse {
  private Long id;
  private String username;
  private String email;
  private String address;
  private Double rating;
}
