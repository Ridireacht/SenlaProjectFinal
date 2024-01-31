package com.senla.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.senla.project.dto.response.UserBriefProfileResponse;
import com.senla.project.entity.User;
import com.senla.project.mapper.UserMapper;
import com.senla.project.repository.UserRepository;
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
  void testGetUserById() {
    Long userId = 1L;

    User user = new User();
    user.setId(userId);
    user.setUsername("testUsername");

    UserBriefProfileResponse expectedUserBriefProfileResponse = new UserBriefProfileResponse();
    expectedUserBriefProfileResponse.setId(user.getId());
    expectedUserBriefProfileResponse.setUsername(user.getUsername());

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userMapper.mapToUserResponse(user)).thenReturn(expectedUserBriefProfileResponse);

    UserBriefProfileResponse actualUserBriefProfileResponse = userService.getUserBriefProfile(userId);

    assertEquals(expectedUserBriefProfileResponse.getId(), actualUserBriefProfileResponse.getId());
    assertEquals(expectedUserBriefProfileResponse.getUsername(), actualUserBriefProfileResponse.getUsername());
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
