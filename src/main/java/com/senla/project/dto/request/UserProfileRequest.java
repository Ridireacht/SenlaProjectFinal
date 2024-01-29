package com.senla.project.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRequest {
  @Email(message = "Email format is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
  @NotBlank(message = "Email must not be blank")
  private String email;

  @NotBlank(message = "Address must not be blank")
  private String address;

  @NotBlank(message = "Password must not be blank")
  private String password;
}
