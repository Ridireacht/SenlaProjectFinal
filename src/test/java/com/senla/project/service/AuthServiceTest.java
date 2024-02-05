package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.senla.project.repository.RoleRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = { AuthServiceImpl.class })
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private RoleRepository roleRepository;

  @Autowired
  private AuthService authService;


  @Test
  public void testDoesUserExistByUsername() {
    String username = "testUser";
    when(userRepository.existsByUsername(username)).thenReturn(true);

    boolean result = authService.doesUserExistByUsername(username);

    assertTrue(result);
  }

  @Test
  public void testDoesUserExistByEmail() {
    String email = "test@example.com";
    when(userRepository.existsByEmail(email)).thenReturn(true);

    boolean result = authService.doesUserExistByEmail(email);

    assertTrue(result);
  }

  @Test
  public void testDoesRoleExistByName() {
    String roleName = "USER";
    when(roleRepository.existsByName(roleName)).thenReturn(true);

    boolean result = authService.doesRoleExistByName(roleName);

    assertTrue(result);
  }
}

