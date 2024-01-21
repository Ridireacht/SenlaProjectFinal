package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.entities.User;
import com.senla.project.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {

  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Test
  public void testMapToUserResponse() {
    User user = new User();
    user.setId(1L);
    user.setUsername("testUser");
    user.setEmail("test@example.com");
    user.setPassword("password");

    UserResponse expectedResponse = new UserResponse();
    expectedResponse.setId(1L);
    expectedResponse.setUsername("testUser");

    UserResponse actualResponse = userMapper.mapToUserResponse(user);

    assertEquals(expectedResponse, actualResponse);
  }
}
