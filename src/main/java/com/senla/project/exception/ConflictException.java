package com.senla.project.exception;

public class ConflictException extends RuntimeException {

  public ConflictException(String reason) {
    super(reason);
  }
}
