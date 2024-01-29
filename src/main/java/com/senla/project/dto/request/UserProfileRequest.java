package com.senla.project.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRequest {
  // Используется иной regex, нежели в RegisterRequest - чтобы пройти валидацию, поле должно быть
  // либо null, либо соответствовать формату email
  @Email(message = "Email format is not valid", regexp = "^(|.*[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+.*)$")
  private String email;

  private String address;

  private String password;
}
