package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.senla.project.dto.request.RegisterRequest;
import com.senla.project.entity.Role;
import com.senla.project.entity.User;
import com.senla.project.repository.RatingRepository;
import com.senla.project.repository.RoleRepository;
import com.senla.project.repository.UserRepository;
import com.senla.project.service.impl.UserDetailsServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = { UserDetailsServiceImpl.class })
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private RoleRepository roleRepository;

  @MockBean
  private RatingRepository ratingRepository;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;


  @Test
  public void testLoadUserByUsername() {
    String username = "testUser";
    User user = createUser(username);

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    assertNotNull(userDetails);
    assertEquals(user.getUsername(), userDetails.getUsername());
  }

  @Test
  public void testRegisterUser() {
    RegisterRequest registerRequest = createRegisterRequest();
    Role role = createRole(registerRequest.getRole());

    when(roleRepository.findByName(registerRequest.getRole())).thenReturn(role);

    String result = userDetailsService.registerUser(registerRequest);

    assertNotNull(result);
    assertTrue(result.contains("New user registered successfully"));
  }

  @Test
  public void testEncodePassword() {
    String password = "testPassword";
    String encodedPassword = "encodedPassword";

    when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

    String result = userDetailsService.encodePassword(password);

    assertNotNull(result);
    assertEquals(encodedPassword, result);
  }

  private User createUser(String username) {
    User user = new User();
    user.setUsername(username);

    Role role = new Role();
    role.setName("USER");

    user.setRole(role);
    return user;
  }

  private RegisterRequest createRegisterRequest() {
    RegisterRequest request = new RegisterRequest();
    request.setUsername("testUser");
    request.setEmail("test@example.com");
    request.setAddress("Test Address");
    request.setPassword("testPassword");
    request.setRole("USER");
    return request;
  }

  private Role createRole(String roleName) {
    Role role = new Role();
    role.setName(roleName);
    return role;
  }
}

