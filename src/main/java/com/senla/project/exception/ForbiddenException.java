package com.senla.project.exception;

public class ForbiddenException extends RuntimeException {

  public ForbiddenException(String reason) {
    super(reason);
  }
}
