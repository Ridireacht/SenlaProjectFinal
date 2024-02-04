package com.senla.project.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRequest {
  @Email(regexp = "^(|.*[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+.*)$", message = "Email must be either null or follow format")
  private String email;

  private String address;

  @Pattern(regexp = "^$|^.{6,}$", message = "Password must be either null or at least 6 characters long")
  private String password;
}
