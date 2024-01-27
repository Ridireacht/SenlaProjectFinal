package com.senla.project.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String entityType, Long entityId) {
    super(entityType + " not found with id: " + entityId + ".");
  }
}
