package com.senla.project.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.senla.project.dto.response.UserResponse;
import com.senla.project.entity.User;
import com.senla.project.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @MockBean
  UserRepository userRepository;

  @Autowired
  UserService userService;


  @Test
  void testGetAllUsers() {
    User user1 = new User();
    User user2 = new User();

    user1.setUsername("testUsername1");
    user2.setUsername("testUsername2");

    when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

    List<UserResponse> result = userService.getAllUsers();

    assertEquals(2, result.size());
    assertThat(result, contains(
        hasProperty("username", is("testUsername1")),
        hasProperty("username", is("testUsername2"))
    ));
  }

  @Test
  void testGetUserById() {
    Long userId = 1L;

    User user = new User();
    user.setId(userId);
    user.setUsername("testUsername");

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    UserResponse result = userService.getUserById(userId);

    assertEquals(user.getId(), result.getId());
    assertEquals(user.getUsername(), result.getUsername());
  }

  @Test
  void testGetUserIdByUsername() {
    String username = "testUsername";

    User user = new User();
    user.setId(1L);
    user.setUsername("testUsername");

    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

    Long result = userService.getUserIdByUsername(username);

    assertEquals(user.getId(), result);
  }

  @Test
  void testDoesUserExist() {
    Long userId = 1L;

    when(userRepository.existsById(userId)).thenReturn(true);

    boolean result = userService.doesUserExist(userId);

    assertTrue(result);
  }
}
