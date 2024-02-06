package com.senla.project.service;

public interface AuthService {

  long getCurrentUserId();

  boolean doesUserExistByUsername(String username);

  boolean doesUserExistByEmail(String email);

  boolean doesRoleExistByName(String name);
}
