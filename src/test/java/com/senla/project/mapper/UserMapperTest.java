package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.entities.User;
import com.senla.project.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {

  @Autowired
  UserMapper mapper;


  @Test
  public void testMapToUserResponse() {
    User user = new User();
    user.setId(1L);
    user.setUsername("testUser");

    UserResponse expectedResponse = new UserResponse();
    expectedResponse.setId(1L);
    expectedResponse.setUsername("testUser");

    UserResponse actualResponse = mapper.mapToUserResponse(user);

    assertEquals(expectedResponse.getId(), actualResponse.getId());
    assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
  }
}
