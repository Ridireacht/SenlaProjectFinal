package com.senla.project.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
  @NotBlank(message = "Username must not be blank")
  private String username;

  @Email(message = "Email format is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
  @NotBlank(message = "Email must not be blank")
  private String email;

  @NotBlank(message = "Address must not be blank")
  private String address;

  @NotBlank(message = "Password must not be blank")
  @Size(min = 6, message = "Password must contain at least 6 symbols")
  private String password;

  @NotBlank(message = "Role must not be blank")
  private String role;
}
