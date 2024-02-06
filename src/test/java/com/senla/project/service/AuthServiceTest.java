package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest(classes = { AuthServiceImpl.class })
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @MockBean
  private UserService userService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private RoleRepository roleRepository;

  @Autowired
  private AuthService authService;


  @Test
  public void testGetCurrentUserId() {
    String expectedUsername = "testUser";

    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);

    SecurityContextHolder.setContext(securityContext);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getName()).thenReturn(expectedUsername);
    when(userService.getUserIdByUsername(expectedUsername)).thenReturn(1L);

    long result = authService.getCurrentUserId();

    assertEquals(1L, result);
  }

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

