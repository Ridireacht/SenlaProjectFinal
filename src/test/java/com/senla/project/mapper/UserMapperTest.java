package com.senla.project.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.senla.project.entity.Rating;
import com.senla.project.entity.User;
import com.senla.project.dto.response.UserResponse;
import com.senla.project.repository.RatingRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

  @Autowired
  UserMapper mapper;

  @MockBean
  RatingRepository ratingRepository;


  @Test
  public void testMapToUserResponse() {
    User user = new User();
    user.setId(1L);
    user.setUsername("testUser");
    user.setAddress("Sample city");

    Rating rating = new Rating();
    rating.setAverageScore(3.5);

    when(ratingRepository.findByUserId(1L)).thenReturn(Optional.of(rating));

    UserResponse expectedResponse = new UserResponse();
    expectedResponse.setId(1L);
    expectedResponse.setUsername("testUser");
    expectedResponse.setAddress("Sample city");
    expectedResponse.setRating(3.5);

    UserResponse actualResponse = mapper.mapToUserResponse(user);

    assertEquals(expectedResponse.getId(), actualResponse.getId());
    assertEquals(expectedResponse.getUsername(), actualResponse.getUsername());
    assertEquals(expectedResponse.getAddress(), actualResponse.getAddress());
    assertEquals(expectedResponse.getRating(), actualResponse.getRating());
  }
}
