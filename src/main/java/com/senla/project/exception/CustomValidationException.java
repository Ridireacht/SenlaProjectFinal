package com.senla.project.exception;

public class CustomValidationException extends RuntimeException {

  public CustomValidationException(String reason) {
    super("Validation failed: " + reason);
  }
}
