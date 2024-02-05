package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senla.project.dto.response.UserBriefProfileResponse;
import com.senla.project.dto.response.UserFullProfileResponse;
import com.senla.project.entity.Rating;
import com.senla.project.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = { UserMapperImpl.class })
public class UserMapperTest {

  @Autowired
  private UserMapper userMapper;

  @Test
  public void testMapToUserBriefProfileResponse() {
    User user = createUser();
    UserBriefProfileResponse userBriefProfileResponse = userMapper.mapToUserBriefProfileResponse(user);

    assertEquals(user.getId(), userBriefProfileResponse.getId());
    assertEquals(user.getUsername(), userBriefProfileResponse.getUsername());
    assertEquals(user.getAddress(), userBriefProfileResponse.getAddress());
    assertEquals(user.getRating().getAverageScore(), userBriefProfileResponse.getRating());
  }

  @Test
  public void testMapToUserFullProfileResponse() {
    User user = createUser();
    UserFullProfileResponse userFullProfileResponse = userMapper.mapToUserFullProfileResponse(user);

    assertEquals(user.getId(), userFullProfileResponse.getId());
    assertEquals(user.getUsername(), userFullProfileResponse.getUsername());
    assertEquals(user.getEmail(), userFullProfileResponse.getEmail());
    assertEquals(user.getAddress(), userFullProfileResponse.getAddress());
    assertEquals(user.getRating().getAverageScore(), userFullProfileResponse.getRating());
  }

  private User createUser() {
    User user = new User();
    user.setId(1L);
    user.setUsername("TestUser");
    user.setEmail("test@example.com");
    user.setAddress("Test Address");
    user.setPassword("TestPassword");

    Rating rating = new Rating();
    rating.setAverageScore(4.5);
    user.setRating(rating);

    return user;
  }
}
