package com.senla.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
  @NotBlank(message = "Username must not be blank")
  private String username;

  @NotBlank(message = "Password must not be blank")
  @Size(min = 6, message = "Password must contain at least 6 symbols")
  private String password;

  @NotBlank(message = "Role must not be blank")
  private String role;
}
