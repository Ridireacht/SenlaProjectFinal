package com.senla.project.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.senla.project.dto.response.UserProfileResponse;
import com.senla.project.dto.response.UserResponse;
import com.senla.project.entity.User;
import com.senla.project.mapper.UserMapper;
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

  @MockBean
  UserMapper userMapper;

  @Autowired
  UserService userService;


  @Test
  void testGetAllUserProfiles() {
    User user1 = new User();
    User user2 = new User();

    user1.setUsername("testUsername1");
    user2.setUsername("testUsername2");

    UserProfileResponse expectedUserResponse1 = new UserProfileResponse();
    UserProfileResponse expectedUserResponse2 = new UserProfileResponse();

    expectedUserResponse1.setUsername(user1.getUsername());
    expectedUserResponse2.setUsername(user2.getUsername());

    when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
    when(userMapper.mapToUserProfileResponse(user1)).thenReturn(expectedUserResponse1);
    when(userMapper.mapToUserProfileResponse(user2)).thenReturn(expectedUserResponse2);

    List<UserProfileResponse> actualUserResponses = userService.getAllUserProfiles();

    assertEquals(2, actualUserResponses.size());
    assertThat(actualUserResponses, contains(
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

    UserResponse expectedUserResponse = new UserResponse();
    expectedUserResponse.setId(user.getId());
    expectedUserResponse.setUsername(user.getUsername());

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userMapper.mapToUserResponse(user)).thenReturn(expectedUserResponse);

    UserResponse actualUserResponse = userService.getUserById(userId);

    assertEquals(expectedUserResponse.getId(), actualUserResponse.getId());
    assertEquals(expectedUserResponse.getUsername(), actualUserResponse.getUsername());
  }

  @Test
  void testGetUserIdByUsername() {
    String username = "testUsername";

    User user = new User();
    user.setId(1L);
    user.setUsername(username);

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
