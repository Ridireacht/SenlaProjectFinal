package com.senla.project.service;

public interface AuthService {

  boolean doesUserExistByUsername(String username);

  boolean doesUserExistByEmail(String email);

  boolean doesRoleExistByName(String name);
}
